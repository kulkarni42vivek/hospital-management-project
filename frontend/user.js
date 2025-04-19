function switchForm(formType) {
  if (formType === "signup") {
    document.getElementById("signup-form").style.display = "block";
    document.getElementById("login-form").style.display = "none";
    document.getElementById("form-title").textContent = "Sign Up";
  } else {
    document.getElementById("signup-form").style.display = "none";
    document.getElementById("login-form").style.display = "block";
    document.getElementById("form-title").textContent = "Login";
  }
}

document.getElementById("signup-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  const data = {
    userId: document.getElementById("signup-userId").value,
    password: document.getElementById("signup-password").value,
    firstName: document.getElementById("signup-firstName").value,
    lastName: document.getElementById("signup-lastName").value,
  };

  try {
    const response = await fetch("http://localhost:8080/api/user/createUser", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
    const result = await response.text();
    alert(result);
    if (result.includes("successfully")) {
      switchForm("login");
    }
  } catch (err) {
    console.error("Error:", err);
    alert("Error occurred while signing up.");
  }
});

document.getElementById("login-form").addEventListener("submit", async (e) => {
  e.preventDefault();

  const data = {
    userId: document.getElementById("login-userId").value,
    password: document.getElementById("login-password").value,
  };

  try {
    const response = await fetch("http://localhost:8080/api/user/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    const result = await response.json();

    if (response.ok) {
      alert(result.message + "\nWelcome, " + result.user.firstName + "!");
      window.location.href = "admin-dashboard.html";
    } else {
      alert(result.message);
    }
  } catch (err) {
    console.error("Error:", err);
    alert("Error occurred while logging in.");
  }
});
