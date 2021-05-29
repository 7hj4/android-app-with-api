<?php 
/**
 * 
 * auther yousef 
 * Date 2021/5/24
 * 
 */
      $host = 'localhost';
      $db_name = 'api_mobile';
      $username = 'root';
      $password = '';


//creating a new connection object using mysqli 
$conn = new mysqli($host, $username, $password, $db_name);
$conn->set_charset("utf8");
 
//if there is some error connecting to the database
//with die we will stop the further execution by displaying a message causing the error 
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
 