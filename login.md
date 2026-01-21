---
layout: default
title: Login page
permalink: /login
---
<html>
<body>
    <div class="main">
        <div class="form-container">
            <form id="login_form">
                <input type="text" placeholder="Username" id="username" required>
                <input type="password" placeholder="Password" id="password" required>
                <button class="button1" type="submit">Login</button>
            </form>
            <center><button class="button2" onclick="window.location.href='{{site.baseurl}}/signup'">Create an Account</button><center>
        </div>
    </div>
<script>
    document.getElementById("login_form").addEventListener("submit", function (event) {
            event.preventDefault();
            login_user();
        });
    function login_user() {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    var raw = JSON.stringify({
        "username": document.getElementById("username").value,
        "password": document.getElementById("password").value
    });
    console.log(raw);
    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        credentials: 'include',
        body: raw,
        redirect: 'follow'
    };
    fetch("http://localhost:8085/authenticate", requestOptions)
    .then(response => {
        if (!response.ok) {
            const errorMsg = 'Login error: ' + response.status;
            console.log(errorMsg);
            switch (response.status) {
                case 401:
                    alert("Incorrect username or password");
                    break;
                case 403:
                    alert("Access forbidden. You do not have permission to access this resource.");
                    break;
                case 404:
                    alert("User not found. Please check your credentials.");
                    break;
                default:
                    alert("Login failed. Please try again later.");
            }
            return Promise.reject('Login failed');
        }
        return response.text()
    })
    .then(result => {
        console.log(result);
        isLoggedIn = true;
        window.location.href = "{{site.baseurl}}/account";
    })
    .catch(error => console.error('Error during login:', error));
}