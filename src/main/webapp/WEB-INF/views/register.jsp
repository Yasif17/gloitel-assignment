<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
</head>
<body>

<h2>Register</h2>

<form id="registerForm">
    <input type="text" id="name" placeholder="Name" required /><br/>
    <input type="email" id="email" placeholder="Email" required /><br/>
    <input type="password" id="password" placeholder="Password" required /><br/>
    <button type="submit">Register</button>
</form>

<p id="message"></p>

<script>
document.getElementById("registerForm").addEventListener("submit", function(e) {
    e.preventDefault();

    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, password })
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw err; });
        }
        return response.json();
    })
    .then(data => {
        document.getElementById("message").innerText = "Registered! Token: " + data.token;
        localStorage.setItem("accessToken", data.token);
        window.location.href = "/home-page"
    })
    .catch(err => {
        document.getElementById("message").innerText = "Error: " + (err.message || "Registration failed");
    });
});
</script>

</body>
</html>