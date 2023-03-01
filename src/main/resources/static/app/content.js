    // 게시글 삭제 클릭 시 동작
    if(document.getElementById("contentDeleteBtn")){
        document.querySelector("#contentDeleteBtn").addEventListener("click", function(e){
            e.preventDefault();

            var wantToDelete = confirm("게시글을 삭제하겠습니까?");
            if(wantToDelete){
                var url = this.getAttribute("href");

                var token = document.querySelector("meta[name=_csrf]").content;
                var header = document.querySelector("meta[name=_csrf_header]").content;

                var promise = fetch(url, {
                    method: "DELETE",
                    headers: {
                        "header": header,
                        "X-Requested-With": "XMLHttpRequest",
                        "X-CSRF-Token": token
                    }
                });

                promise.then(function(response){
                    return response.json();
                }).then(function(data){
                    if(data.path != null){
                        console.log(data);
                        location.href = data.path;
                    } else if(data.beDeleted){
                        alert("게시글을 삭제했습니다.");
                        location.href = "/";
                    } else {
                        alert("게시글을 삭제할 수 없습니다. 문제가 반복된다면 문의 바랍니다.");
                    }
                });
            }
        });
    }

    // 답글 클릭 시 focus 이동 및 동작
    function reply(btn){
        document.querySelector("#replyIcon").style.display = "block";
        document.querySelector("#groupId").value = btn.dataset.num;
        document.querySelector("#comment").focus();
        document.querySelector("#comment").value = "@" + btn.dataset.text + " ";
    }

    // 취소 클릭 시
    if(document.getElementById("replyCancel")){
        document.querySelector("#replyCancel").addEventListener("click", function(e){
            e.preventDefault();

            document.querySelector("#replyIcon").style.display = "none";
            document.querySelector("#groupId").value = "";
            document.querySelector("#comment").value = "";
        });
    }

    // 댓글 작성 form submit
    if(document.getElementById("commentForm")){
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
    }

    // 댓글 삭제 시 동작
    function deleteReply(btn){
        // 삭제 의도를 파악한 후
        var commentDelete = confirm("댓글을 삭제하시겠습니까?");

        if(commentDelete){
            var url = "/user/comments/" + btn.dataset.num;

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
                // 이것은 작성자가 다른 요청의 경우 data에 beDelete 대신 에러 data가 나오는데
                // 그 중에서 path 정보가 있다.
                if(data.path != null){
                    location.href = data.path;
                } else if(data.beDeleted){
                    alert("댓글을 삭제하였습니다.");
                    document.querySelector("#input" + data.commentId).value = "삭제된 댓글입니다.";
                    document.querySelector("#reply" + data.commentId).remove();
                    document.querySelector("#delete" + data.commentId).remove();
                } else if(!data.beDeleted){
                    alert("댓글 삭제에 실패하였습니다. 문제가 반복된다면 문의 바랍니다.");
                }
            });
        }
    }

    // 댓글 pagination
    if(document.getElementById("moreCommentBtn")){
        document.querySelector("#moreCommentBtn").addEventListener("click", function(){
            // 추가 댓글이 있는지 validation
            if(totalCommentPages > pageNum){
                var url = "/contents/" + contentId + "/comments/" + pageNum;
                var promise = fetch(url);

                promise.then(function(response){
                    return response.text();
                }).then(function(data){
                    var dom = new DOMParser();
                    dom = dom.parseFromString(data, "text/html");
                    var newDiv = dom.getElementsByClassName("comment");

                    var tempArray = Array.from(newDiv);
                    tempArray.forEach(tmp => {
                        document.querySelector("#commentDiv").appendChild(tmp);
                    });

                    pageNum++;

                    // 댓글 전체를 가져왔을 경우 같아지는데, 이때 버튼을 없앰
                    if(pageNum == totalCommentPages){
                        document.querySelector("#moreCommentBtn").remove();
                    }
                });
            }
        });
    }