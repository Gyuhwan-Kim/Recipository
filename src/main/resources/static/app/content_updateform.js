    window.onload = function(){
        var closeBtns = document.querySelectorAll(".btn-close");
        closeBtns.forEach(tmp => {
            tmp.addEventListener("click", function(e){
                e.preventDefault();

                tmp.parentElement.remove();
            });
        });
    }

    // 이미지 업로드 버튼을 눌렀을 때 input type file 강제 클릭
    document.querySelector("#imageUploadBtn").addEventListener("click", function(e){
        e.preventDefault();

        document.querySelector("#imageFile").click();
    });

    // 링크 추가
    document.querySelector("#linkAddBtn").addEventListener("click", function(e){
        e.preventDefault();

        var linkList = document.querySelector("#linkList");

        var link = document.querySelector(".linkForm");
        var newLink = link.cloneNode(true);

        newLink.children[0].value = "";
        newLink.children[1].style.display = "block";

        linkList.appendChild(newLink);
        delLink();
    });

    // 새로 만든 링크 삭제 이벤트 부여 함수
    function delLink(){
        var closeBtns = document.querySelectorAll(".btn-close");
        var length = closeBtns.length;
        closeBtns[length - 1].addEventListener("click", function(e){
            e.preventDefault();

            this.parentElement.remove();
        });
    }

    // 게시글 작성 form submit
    document.querySelector("#contentForm").addEventListener("submit", function(e){
        e.preventDefault();

        var url = document.querySelector("#contentForm").action;
        var formData = new FormData(this);

        var token = document.querySelector("meta[name=_csrf]").content;
        var header = document.querySelector("meta[name=_csrf_header]").content;

        var promise = fetch(url, {
            method: "POST",
            headers: {
                "header": header,
                "X-Requested-With": "XMLHttpRequest",
//                "Content-Type": "multipart/form-data",
                "X-CSRF-Token": token
            },
            body: formData //JSON.stringify(object)
        });

        promise.then(function(response){
            return response.json();
        }).then(function(data){
            if(data.beSaved){
                alert("게시글을 수정했습니다.");
//                location.href = "/";
            } else {
                alert("게시글 수정에 실패했습니다. 문제가 반복된다면 문의 바랍니다.");
            }
        });
    });

    // 이미지 파일을 선택했을 때 동작하는 method (썸네일)
    document.querySelector("#imageFile").addEventListener("change", function(e){
        readImage(e.target, "#image");
    });

    // 이미지를 읽는 function
    function readImage(input, imageID) {
        // 이미지 파일인지 검사
        var fileName = input.files[0].name;
        var fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
        fileExt = fileExt.toLowerCase();

        if("jpg" != fileExt && "jpeg" != fileExt && "png" != fileExt && "bmp" != fileExt) {
            alert("이미지 파일만 선택할 수 있습니다.");
            return;
        }

        // FileReader 인스턴스 생성
        var reader = new FileReader();
        // 이미지가 로드가 된 경우
        reader.onload = function(e){
            var previewImg = document.querySelector(imageID);
            console.log(e.target.result);
            previewImg.src = e.target.result;
        }
        // reader가 이미지 읽도록 하기
        reader.readAsDataURL(input.files[0]);
    }