<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<h2>Welcome! <span id="userName"></span> </h2>
<button id="logoutBtn">Logout</button>
<p id="message"></p>

<div id="adminPanel" style="display:none;">
    <h3>Admin Controls</h3>
    <button onclick="goToManageUsers()">Manage Users</button>
</div>

<table id="userTable" border="1" style="display:none;">
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Role</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody id="userTableBody"></tbody>
</table>

<div id="editFormSection" style="display:none; margin-top:20px;">
    <h3>Edit User</h3>
    <input type="hidden" id="editUserId" />
    <label>Name: <input type="text" id="editName" /></label><br/>
    <label>Email: <input type="text" id="editEmail" /></label><br/>
    <label>Role:
        <select id="editRole">
            <option value="">-- no change --</option>
            <option value="USER">USER</option>
            <option value="ADMIN">ADMIN</option>
        </select>
    </label><br/>
    <button onclick="submitEdit()">Save Changes</button>
    <button onclick="document.getElementById('editFormSection').style.display='none'">Cancel</button>
</div>


<script>

    let allUsers = [];  // fetch all global users.


    function goToManageUsers() {
        const token = localStorage.getItem("accessToken");

        fetch("/api/admin/users", {
            headers: { "Authorization": "Bearer " + token }
        })
            .then(res => res.json())
            .then(users => {
                allUsers = users;
                // console.log(users);
                // console.log("tbody element:", document.getElementById("userTableBody"));
                const tbody = document.getElementById("userTableBody");
                tbody.innerHTML = ""; // clear old rows

                users.forEach(u => {
                    const row = "<tr>" +
                        "<td>" + u.id + "</td>" +
                        "<td>" + u.name + "</td>" +
                        "<td>" + u.email + "</td>" +
                        "<td>" +
                        "<select id='role-" + u.id + "'>" +
                        "<option value='USER'" + (u.role === 'USER' ? ' selected' : '') + ">USER</option>" +
                        "<option value='ADMIN'" + (u.role === 'ADMIN' ? ' selected' : '') + ">ADMIN</option>" +
                        "</select>" +
                        "</td>" +
                        "<td>" +
                        // "<button onclick='updateRole(" + u.id + ")'>Update</button>" +
                        "<button onclick='editUser(" + u.id + ")'>Update</button>" +
                        "</td>" +
                        "</tr>";
                    tbody.innerHTML += row;
                });

                document.getElementById("userTable").style.display = "table";
            })
            .catch(err => console.error(err));
    }

    function editUser(userId) {
        const user = allUsers.find(u => u.id === userId);
        if (!user) return;

        document.getElementById("editUserId").value = user.id;
        document.getElementById("editName").value = user.name;
        document.getElementById("editEmail").value = user.email;
        document.getElementById("editRole").value = ""; // default "no change"

        document.getElementById("editFormSection").style.display = "block";
    }

    function submitEdit() {
        const token = localStorage.getItem("accessToken");
        const userId = document.getElementById("editUserId").value;

        const name = document.getElementById("editName").value.trim();
        const email = document.getElementById("editEmail").value.trim();
        const role = document.getElementById("editRole").value;

        const payload = {};
        if (name !== "") payload.name = name;
        if (email !== "") payload.email = email;
        if (role !== "") payload.role = role;

        fetch("/api/admin/users/" + userId, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(payload)
        })
            .then(res => {
                if (!res.ok) return res.json().then(err => { throw err; });
                return res.json();
            })
            .then(updated => {
                alert("User updated!");
                document.getElementById("editFormSection").style.display = "none";
                goToManageUsers(); // refresh table with new data
            })
            .catch(err => alert("Error: " + (err.message || "Update failed")));
    }

    function updateRole(userId) {
        const token = localStorage.getItem("accessToken");
        const selectedRole = document.getElementById("role-" + userId).value;

        fetch("/api/admin/users/" + userId + "/role", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({ role: selectedRole })
        })
            .then(res => {
                if (!res.ok) {
                    return res.json().then(err => { throw err; });
                }
                alert("Role updated successfully!");
            })
            .catch(err => {
                alert("Error: " + (err.message || "Update failed"));
            });
    }

</script>


<script>
    const role = localStorage.getItem("userRole");
    if (role === "ADMIN") {
        document.getElementById("adminPanel").style.display = "block";
    }


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