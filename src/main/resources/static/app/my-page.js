    document.querySelector("#profileBtn").addEventListener("click", function(e){
        var url = "/user/my-profile";

        var promise = fetch(url);

        promise.then(function(response){
            return response.text();
        }).then(function(data){
            var dom = new DOMParser();
            dom = dom.parseFromString(data, "text/html");
            console.log(dom);
            var newDiv = dom.getElementsByClassName("templateDiv")[0];
            console.log(newDiv);
            document.querySelector(".templateDiv").replaceWith(newDiv);

            addProfileBtnEvent();
        });
    });

    document.querySelector("#deleteBtn").addEventListener("click", function(e){
        var url = "/user/delete-form";

        var promise = fetch(url);

        promise.then(function(response){
            return response.text();
        }).then(function(data){
            var dom = new DOMParser();
            dom = dom.parseFromString(data, "text/html");
            console.log(dom);
            var newDiv = dom.getElementsByClassName("templateDiv")[0];
            console.log(newDiv);
            document.querySelector(".templateDiv").replaceWith(newDiv);

            addContentsDeleteBtnEvent();
        });
    });

    // Profile 버튼 event 추가 function
    function addProfileBtnEvent(){
        // Profile 변경 template loading
        document.querySelector("#profileFormBtn").addEventListener("click", function(e){
            var url = "/user/profile-form";

            var promise = fetch(url);

            promise.then(function(response){
                return response.text();
            }).then(function(data){
                var dom = new DOMParser();
                dom = dom.parseFromString(data, "text/html");
                var newDiv = dom.getElementById("profileForm");
                document.querySelector("#myProfile").replaceWith(newDiv);

                addProfileEvent();
            });
        });

        // Password 변경 template loading
        document.querySelector("#pwdFormBtn").addEventListener("click", function(e){
            var url = "/user/password-form";

            var promise = fetch(url);

            promise.then(function(response){
                return response.text();
            }).then(function(data){
                var dom = new DOMParser();
                dom = dom.parseFromString(data, "text/html");
                var newDiv = dom.getElementById("pwdForm");
                document.querySelector("#myProfile").replaceWith(newDiv);

                addPwdEvent();
            });
        });
    }

    var nameValidation = true;
    var pwdValidation = false;

    // Profile 변경 event 추가 function
    function addProfileEvent(){
        // name constraint
        document.querySelector("#name").addEventListener("input", function(){
            nameValidation = false;
            var regex = /^[a-zA-Z0-9가-힁]{4,12}$/;

            var duplCheckBtn = document.querySelector(".duplCheckBtn");
            var name = duplCheckBtn.getAttribute("data");

            if(this.value == name){
                document.querySelector("#nameConstraint").innerText = "변경하려면 다른 닉네임을 작성해주세요.";
                nameValidation = true;
                document.querySelector("#nameDuplCheck").value = true;
                duplCheckBtn.style.display = "none";
            } else if(this.value == "" || !regex.test(this.value)){
                document.querySelector("#nameConstraint").innerText = "4~12자로 입력해주세요.";
                nameValidation = false;
                document.querySelector("#nameDuplCheck").value = false;
                duplCheckBtn.style.display = "block";
            } else if(regex.test(this.value)){
                document.querySelector("#nameConstraint").innerText = "중복 확인이 필요합니다.";
                nameValidation = true;
                document.querySelector("#nameDuplCheck").value = false;
                duplCheckBtn.style.display = "block";
            }
        });

        // 이메일, 닉네임 중복 확인
        document.querySelector(".duplCheckBtn").addEventListener("click", function(e){
            e.preventDefault();

            var url = "/duplcheck";

            var name = document.querySelector("#name").value;
            var object = {name};

            duplCheck(url, object, "#name", "닉네임");
        });

        // 프로필 변경 클릭 시 동작
        document.querySelector("#profileUpdateForm").addEventListener("submit", function(e){
            e.preventDefault();

            var nameDuplCheck = document.querySelector("#nameDuplCheck").value;
            var formValidation = nameValidation && nameDuplCheck;

            if(formValidation){
                var url = this.action;
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
                    if(data.beUpdated){
                        alert("닉네임 정보를 변경했습니다.");
                        location.href = "/user/my-page";
                    } else {
                        if(data.errorMessage != null){
                            var errorList = Object.entries(data.errorMessage);
                            errorList.forEach(error => {
                                document.querySelector("#" + error[0] + "Constraint").innerText = error[1];
                            });
                        }
                        alert("닉네임을 변경할 수 없습니다. 문제가 반복된다면 문의 바랍니다.");
                    }
                });
            } else if(!nameValidation){
                alert("닉네임 정보를 올바르게 입력해주세요.");
            } else if(!nameDuplCheck){
                alert("닉네임 중복 확인이 필요합니다.");
            }
        });
    }

    // Password 변경 event 추가 function
    function addPwdEvent(){
        // password constraint
        document.querySelector("#password").addEventListener("input", function(){
            var regex = /^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,20}$/;

            if(this.value == "" || !regex.test(this.value)){
                document.querySelector("#passwordConstraint").innerText = "영문과 숫자를 합쳐 8자 이상 20자 이하로 입력해주세요.";
            } else {
                document.querySelector("#passwordConstraint").innerText = "사용 가능한 비밀번호 입니다.";
            }

            pwdCheck();
        });

        // 비밀 번호 check
        document.querySelector("#pwdCheck").addEventListener("input", pwdCheck);

        // 프로필 변경 클릭 시 동작
        document.querySelector("#pwdUpdateForm").addEventListener("submit", function(e){
            e.preventDefault();

            if(pwdValidation){
                var url = this.action;
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
                    if(data.beUpdated){
                        location.href = "/";
                        alert("비밀번호를 변경했습니다. 다시 로그인해주시기 바랍니다.");
                    } else {
                        if(data.errorMessage != null){
                            var errorList = Object.entries(data.errorMessage);
                            errorList.forEach(error => {
                                document.querySelector("#" + error[0] + "Constraint").innerText = error[1];
                            });
                        }
                        alert("비밀번호를 변경할 수 없습니다. 문제가 반복된다면 문의 바랍니다.");
                    }
                });
            } else if(!pwdValidation){
                alert("비밀번호 정보들을 올바르게 입력해주세요.");
            }
        });
    }

    // 이메일, 닉네임 중복확인 function
    function duplCheck(url, object, target1, target2){
        var token = document.querySelector("meta[name=_csrf]").content;
        var header = document.querySelector("meta[name=_csrf_header]").content;

        var promise = fetch(url, {
                method: "POST",
                headers: {
                    "header": header,
                    "X-Requested-With": "XMLHttpRequest",
                    "Content-Type": "application/json",
                    "X-CSRF-Token": token
                },
                body: JSON.stringify(object)
        });

        promise.then(function(response) {
            return response.json();
        }).then(function(data){
            if(data.isChecked){
                document.querySelector(target1 + "Constraint").innerText = "다른 "+ target2 + "을 사용해주세요";
                document.querySelector(target1 + "DuplCheck").value = false;
            } else {
                document.querySelector(target1 + "Constraint").innerText = "사용 가능한 " + target2 + "입니다.";
                document.querySelector(target1 + "DuplCheck").value = true;
            }
        });
    }

    // 비밀 번호 확인 function
    function pwdCheck(){
        var password = document.querySelector("#password").value;
        var pwdCheck = document.querySelector("#pwdCheck").value;
        var regex = /^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,20}$/;

        if(password != pwdCheck){
            document.querySelector("#pwdCheckMsg").innerText = "비밀번호가 일치하지 않습니다.";
            pwdValidation = false;
        } else if(!regex.test(password) && password == pwdCheck){
            document.querySelector("#pwdCheckMsg").innerText = "비밀번호가 일치합니다.";
            pwdValidation = false;
        } else if(regex.test(password) && password == pwdCheck){
            document.querySelector("#pwdCheckMsg").innerText = "비밀번호가 일치합니다.";
            pwdValidation = true;
        }
    }

    // 게시글 일괄 삭제 event 추가 function
    function addContentsDeleteBtnEvent(){
        document.querySelector("#contentsDeleteForm").addEventListener("submit", function(e){
            e.preventDefault();

            var deletion = confirm("선택한 게시글들을 삭제하시겠습니까?");
            if(deletion){
                var url = this.action;
                var formData = new FormData(this);

                var token = document.querySelector("meta[name=_csrf]").content;
                var header = document.querySelector("meta[name=_csrf_header]").content;

                var promise = fetch(url, {
                    method: "DELETE",
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
                    if(data.beDeleted){
                        alert("게시글을 삭제했습니다.");
                        location.href = "/user/my-page";
                    } else {
                        alert("게시글을 삭제하지 못했습니다. 문제가 반복된다면 문의 바랍니다.");
                    }
                });
            }
        });
    }