<?php
$servername="localhost";
$username="root";
$password=" ";
$dbname="Appointments";
$conn= new mysqli($servername,$username,$password,$dbname);
if ($conn->connect_error){
    die("Connection failed: ". $conn->connect_error);
}
if($_SERVER["REQUEST_METHOD"] == "POST"){

    $SlNo=$_POST["SLNO"];
    $Name=$_POST["NAME"];
    $Country= $_POST["COUNTRY"];
    $ContInfo = $_POST["CONTINFO"];
    $Issue= $_POST["ISSUE"];
    
    $sql= "INSERT INTO Users(SLNO,NAME,COUNTRY,CONTINFO,ISSUE) VALUES('$SlNo','$Name',' $Country','$ContInfo','$Issue')";
    if($conn->query($sql) == TRUE) {
        echo "Appointment Booking Successful";
    }
    else {
        echo "ERROR:." .$sql. "<br>". $conn->error;
    }
}
$conn->close();
?>