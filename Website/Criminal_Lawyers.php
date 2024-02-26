<?php
$servername="localhost";
$username="root";
$password=" ";
$dbname="Criminal_Lawyers";
$conn= new mysqli($servername,$username,$password,$dbname);
if ($conn->connect_error){
    die("Connection failed: ". $conn->connect_error);
}
if($_SERVER["REQUEST_METHOD"] == "POST"){
    $SlNo=$_POST["SLNO"];
    $Name=$_POST["NAME"];
    $ContInfo= $_POST["CONTINFO"];
    $BarCounNo= $_POST["BARCOUNNO"];
    $OffAddress= $_POST["OFFADDRESS"];
    $Price= $_POST["PRICE"];
   
    $sql= "INSERT INTO Users(SLNO,NAME,CONTINFO,BARCOUNNO,OFFADDRESS,PRICE) VALUES('$SlNo','$Name','$ContInfo','$BarCounNo','$OffAddress','$Price')";
    if($conn->query($sql) == TRUE) {
        echo "Criminal Lawyer Registration Successful";
    }
    else {
        echo "ERROR:." .$sql. "<br>". $conn->error;
    }
}
$conn->close();
?>