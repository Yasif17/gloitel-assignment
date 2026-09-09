<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
</head>
<body>

<h2>Welcome! <span id="userName"></span> </h2>
<button id="logoutBtn">Logout</button>
<p id="message"></p>

<script>

document.getElementById("userName").innerText = localStorage.getItem("userName");

document.getElementById("logoutBtn").addEventListener("click", function() {
    const token = localStorage.getItem("accessToken");

    fetch("/api/auth/logout", {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw err; });
        }
        localStorage.removeItem("accessToken");
        document.getElementById("message").innerText = "Logged out!";
        window.location.href = "/login-page";
    })
    .catch(err => {
        document.getElementById("message").innerText = "Error: " + (err.message || "Logout failed");
    });
});
</script>

</body>
</html>