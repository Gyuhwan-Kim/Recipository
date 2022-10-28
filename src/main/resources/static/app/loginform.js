    document.querySelector("#loginForm").addEventListener("submit", function(e){
        e.preventDefault();

        var url = this.action;
        var formData = new FormData(this);
        var object = formToJSON(formData);

        var token = document.querySelector("meta[name=_csrf]").content;
        var header = document.querySelector("meta[name=_csrf_header]").content;

        let promise = fetch(url, {
            method: "POST",
            headers: {
                "header": header,
                "X-Requested-With": "XMLHttpRequest",
                "Content-Type": "application/json",
                "X-CSRF-Token": token
            },
            body: JSON.stringify(object)
        });

        promise.then(function(response){
            return response.json();
        }).then(function(data){
            if(data.isSuccess){
                alert("로그인 되었습니다.");
                location.href = "/";
            } else {
                alert("아이디와 비밀번호를 다시 확인해주세요.");
            }
        });
    });

    function formToJSON(formData){
        var obj = {};
        var data = formData.entries();
        formData.forEach((value, key) => {
           obj[key] = value;
        });

        return obj;
    }