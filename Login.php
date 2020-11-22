<?php
    $con = mysqli_connect("localhost", "moapp", "wodnjs13!", "moapp");
    mysqli_query($con,'SET NAMES utf8');

    $id = $_POST["id"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM USER WHERE id = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $id, $password);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $id, $password, $usr_name);

    $response = array();
    $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["id"] = $id;
        $response["password"] = $password;
        $response["usr_name"] = $usr_name;     
    }

    echo json_encode($response);

?>
