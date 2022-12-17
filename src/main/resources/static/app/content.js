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

    // 댓글 작성 form submit
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

    // 댓글 삭제 시 동작
    document.querySelectorAll(".commentDeleteBtn").forEach(tmp => {
        tmp.addEventListener("click", function(e){
            e.preventDefault();

            // 삭제 의도를 파악한 후
            var commentDelete = confirm("댓글을 삭제하시겠습니까?");

            if(commentDelete){
                var url = this.href;

                var token = document.querySelector("meta[name=_csrf]").content;
                var header = document.querySelector("meta[name=_csrf_header]").content;

                var promise = fetch(url, {
                    method: "PUT",
                    headers: {
                        "header": header,
                        "X-Requested-With": "XMLHttpRequest",
                        "X-CSRF-Token": token
                    }
                });

                promise.then(function(response){
                    return response.json();
                }).then(function(data){
                    // 삭제 성공 여부에 따라 다른 동작을 하도록
                    if(data.beDeleted){
                        alert("댓글을 삭제하였습니다.");
                        document.querySelector("#input" + data.commentId).value = "삭제된 댓글입니다.";
                        document.querySelector("#reply" + data.commentId).remove();
                        document.querySelector("#delete" + data.commentId).remove();
                    } else {
                        alert("댓글 삭제에 실패하였습니다. 문제가 반복된다면 문의 바랍니다.");
                    }
                });
            }
        });
    });