<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<h2>Welcome!</h2>
<button id="logoutBtn">Logout</button>
<p id="message"></p>

<script>
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