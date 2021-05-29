<?php

include '../config/config.php'; 

$response = array();

 if(isset($_POST['email']) && isset($_POST['password'])){
 
    //getting values 
    $email = $_POST['email'];
    $password = md5($_POST['password']); 
    
    //creating the query 
    $stmt = $conn->prepare("SELECT id, username, email FROM users WHERE email = ? AND password = ?");
    $stmt->bind_param("ss",$email, $password);
    
    $stmt->execute();
    
    $stmt->store_result();
    
    //if the user exist with given credentials 
    if($stmt->num_rows > 0){
    
    $stmt->bind_result($id, $username, $email);
    $stmt->fetch();
    
    $user = array(
    'id'=>$id, 
    'username'=>$username, 
    'email'=>$email,
    );
    
    $response['errors'] = false; 
    $response['message'] = 'Login successfull'; 
    $response['user'] = $user;
    echo json_encode($response);
    }else{
    //if the user not found 
    $response['errors'] = true; 
    $response['message'] = 'Invalid username or password';
     echo json_encode($response);
    }
   }else{
      $response['errors'] = true; 
      $response['message'] = 'check paramater invalid';
      echo json_encode($response);
   }
   

?>