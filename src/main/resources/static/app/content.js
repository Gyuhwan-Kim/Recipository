    // 답글 클릭 시 focus 이동 및 동작
    document.querySelectorAll(".replyBtn").forEach(tmp => {
        tmp.addEventListener("click", function(e){
            document.querySelector("#replyIcon").style.display = "block";
            document.querySelector("#groupId").value = this.dataset.num;
            document.querySelector("#comment").focus();
            document.querySelector("#comment").value = "@" + this.dataset.text + " ";
        });
    });
    // 취소 클릭 시
    document.querySelector("#replyCancel").addEventListener("click", function(e){
        e.preventDefault();

        document.querySelector("#replyIcon").style.display = "none";
        document.querySelector("#groupId").value = "";
        document.querySelector("#comment").value = "";
    });

    // 게시글 작성 form submit
    document.querySelector("#commentForm").addEventListener("submit", function(e){
        e.preventDefault();

        var url = document.querySelector("#commentForm").action;
        var formData = new FormData(this);

        var token = document.querySelector("meta[name=_csrf]").content;
        var header = document.querySelector("meta[name=_csrf_header]").content;

        var promise = fetch(url, {
            method: "POST",
            headers: {
                "header": header,
                "X-Requested-With": "XMLHttpRequest",
                "X-CSRF-Token": token
            },
            body: formData
        });

        promise.then(function(response){
            return response.json();
        }).then(function(data){
            if(data.beAdded){
                location.reload();
            } else {
                alert("댓글을 추가할 수 없습니다. 문제가 반복된다면 문의 바랍니다.");
            }
        });
    });