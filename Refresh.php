<?php
    $con = mysqli_connect("localhost", "hongdroid94", "password", "hongdroid94");
    $sql = "SELECT waher_num, washer_state, finish_time FROM WASHER";
    $result = mysqli_query($con, $sql);
    $data = array();
    if ($result) {
        while($row = mysqli_fetch_assoc($result)) {
            array_push($data, array('waher_num'=>$row[0], 'waher_state'=>$row[1], 'finish_time'=>$row[2]));
        }
        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("refresh"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }else{
        echo "SQL ERROR";
    }
    mysqli_close($con); // 디비 접속 닫기
?>
