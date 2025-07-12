<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Pension on Higher Wages</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f4f8;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            background: #ffffff;
            padding: 2rem 3rem;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        h2 {
            color: #333333;
            margin-bottom: 1.5rem;
        }

        label {
            display: block;
            font-weight: bold;
            margin-bottom: 0.5rem;
            color: #555555;
        }

        input[type="text"] {
            width: 100%;
            padding: 0.5rem;
            border: 1px solid #cccccc;
            border-radius: 5px;
            margin-bottom: 1rem;
            font-size: 1rem;
        }

        button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 0.75rem 1.5rem;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Pension on Higher Wages- Detail Report</h2>
        <form id="pdfForm" method="GET" action="" onsubmit="return mergePdf(event)">
            <label for="empCode">Employee Code</label>
            <input type="text" id="empCode" name="empCode" required>
            <button type="submit">Download Report</button>
        </form>
    </div>

    <script>
    function mergePdf(event) {
        event.preventDefault();  // Prevent actual form submission
        const empCode = document.getElementById("empCode").value.trim();
        if (!empCode) {
            alert("Please enter Employee Code.");
            return false;
        }
        //const url = '/pohw-report/api/pdf/merge?empCode=' + encodeURIComponent(empCode);
        const url = 'https://dpl.net.in/pohw-report/api/pdf/merge?empCode=' + encodeURIComponent(empCode);

        window.location.href = url;
        return false;  // Prevent default
    }
</script>

</body>
</html>
