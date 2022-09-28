
    var emailValidation = false;
    var nameValidation = false;
    var pwdValidation = false;

    // email constraint
    document.querySelector("#email").addEventListener("input", function(){
        emailValidation = false;
        var regex = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-z]{2,3}$/;

        if(!regex.test(this.value)) {
            document.querySelector("#emailCheckMsg").innerText = "이메일 양식에 맞게 작성해주세요.";
            emailValidation = false;
        } else {
            document.querySelector("#emailCheckMsg").innerText = "중복 확인이 필요합니다.";
            emailValidation = true;
        }
    });

    // name constraint
    document.querySelector("#name").addEventListener("input", function(){
        nameValidation = false;
        var regex = /^[a-zA-Z0-9가-힁]{1,12}$/;

        if(this.value == ""){
            document.querySelector("#nameCheckMsg").innerText = "8~12자로 입력해주세요.";
            nameValidation = false;
        } else if(!regex.test(this.value)) {
            document.querySelector("#nameCheckMsg").innerText = "닉네임 양식에 맞게 작성해주세요.";
            nameValidation = false;
        } else if(regex.test(this.value)){
            document.querySelector("#nameCheckMsg").innerText = "중복 확인이 필요합니다.";
            nameValidation = true;
        }
    });

    // 이메일, 닉네임 중복 확인
    document.querySelectorAll(".duplCheckBtn").forEach(tmp => {
        tmp.addEventListener("click", function(e){
            e.preventDefault();

            var email;
            var name;
            var object;

            var url = "/duplcheck";

            if(tmp.classList.contains("emailCheck") && emailValidation){
                email = document.querySelector("#email").value;
                object = {email};

                duplCheck(url, object, "#email", "이메일");
            } else if(tmp.classList.contains("nameCheck") && nameValidation){
                name = document.querySelector("#name").value;
                object = {name};

                duplCheck(url, object, "#name", "닉네임");
            }
        });
    });

    // 비밀 번호 constraint
    document.querySelector("#password").addEventListener("input", function(){
        var regex = /^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,20}$/;

        if(this.value == ""){
            document.querySelector("#pwdConstraint").innerText = "영문과 숫자를 합쳐 8자 이상 20자 이하로 입력해주세요.";
        } else if(regex.test(this.value)){
            document.querySelector("#pwdConstraint").innerText = "";
        } else {
            document.querySelector("#pwdConstraint").innerText = "사용할 수 없는 비밀번호 입니다.";
        }

        pwdCheck();
    });

    // 비밀 번호 check
    document.querySelector("#pwdCheck").addEventListener("input", pwdCheck);

    document.querySelector("#signinForm").addEventListener("submit", function(e){
        e.preventDefault();

        var emailDuplCheck = document.querySelector("#emailDuplCheck").value
        var nameDuplCheck = document.querySelector("#nameDuplCheck").value;
        var emailCheck = emailValidation && emailDuplCheck;
        var nameCheck = nameValidation && nameDuplCheck;
        var formValidation = emailCheck && nameCheck && pwdValidation;

        if(formValidation){
            var url = this.action;
            var formData = new FormData(this);

            for(var value of formData.values()){
                console.log(value);
            }

            var object = formToJSON(formData);

            var queryString = new URLSearchParams(new FormData(this)).toString();

            var promise = fetch(url, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(object)
            });

            promise.then(function(response){
                return response.json();
            }).then(function(data){
                if(data.beSuccess){
                    alert("회원 가입에 성공하였습니다. 로그인 후 이용해주세요.");
                    location.href = "/loginform";
                } else {
                    alert("회원 가입에 실패하였습니다. 문제가 반복된다면 문의 바랍니다.");
                }
            });

        } else if(!emailValidation){
            alert("이메일 정보를 올바르게 입력해주세요.");
        } else if(!emailDuplCheck){
            alert("이메일 중복 확인이 필요합니다.");
        } else if(!nameValidation){
            alert("닉네임 정보를 올바르게 입력해주세요.");
        } else if(!nameDuplCheck){
            alert("닉네임 중복 확인이 필요합니다.");
        } else if(!pwdValidation){
            alert("비밀 번호 정보들을 올바르게 입력해주세요.");
        } else if(formValidation){
            alert("회원 가입 절차 시작");
        }
    });

    // 이메일, 닉네임 중복확인 function
    function duplCheck(url, object, target1, target2){
        var promise = fetch(url, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(object)
        });

        promise.then(function(response) {
            return response.json();
        }).then(function(data){
            if(data.isChecked){
                document.querySelector(target1 + "CheckMsg").innerText = "다른 "+ target2 + "을 사용해주세요";
                document.querySelector(target1 + "DuplCheck").value = false;
            } else {
                document.querySelector(target1 + "CheckMsg").innerText = "사용 가능한 " + target2 + "입니다.";
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

    // form data to JSON
    function formToJSON(formData){
        var obj = {};
        var data = formData.entries();
        formData.forEach((value, key) => {
           obj[key] = value;
        });

        return obj;
    }
