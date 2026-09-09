<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>

<h2>Login</h2>

<form id="loginForm">
    <input type="email" id="email" placeholder="Email" required /><br/>
    <input type="password" id="password" placeholder="Password" required /><br/>
    <button type="submit">Login</button>
</form>

<p id="message"></p>

<script>
document.getElementById("loginForm").addEventListener("submit", function(e) {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw err; });
        }
        return response.json();
    })
    .then(data => {
        localStorage.setItem("accessToken", data.token);
        localStorage.setItem("userName",data.name);
        document.getElementById("message").innerText = "Login successful!";
        window.location.href = "/home-page"; // redirect after login
    })
    .catch(err => {
        document.getElementById("message").innerText = "Error: " + (err.message || "Login failed");
    });
});
</script>

</body>
</html>