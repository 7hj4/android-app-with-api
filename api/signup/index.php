<?php 
include '../config/config.php'; 

$response = array();
        
if(isset($_POST['username']) && isset($_POST['email']) && isset($_POST['password'])){

    $username = htmlentities($_POST['username']); 
    $email = htmlentities($_POST['email']); 
    $password = htmlentities($_POST['password']);
    $md5 = md5($password);
    $date = date("Y-m-d H:i:s");
    
    // api Kay 
    $api = md5(rand(1, 99999));

    $stmt = $conn->prepare("SELECT id , email FROM users WHERE email = ?");
    $stmt->bind_param("s",$email);
    $stmt->execute();
    $stmt->store_result();
    
    if($stmt->num_rows > 0){
    $response['errors'] = true;
    $response['message'] = 'email already registered';
    $stmt->close();
    echo json_encode($response); 
    }else{
    $stmt = $conn->prepare("INSERT INTO users (username, email, password ,api_kay, created) VALUES (?, ?, ? ,?, ?)");
    $stmt->bind_param("sssss", $username, $email, $md5, $api ,$date);
    
    if($stmt->execute()){
    $stmt = $conn->prepare("SELECT id, username, email FROM users WHERE email = ?"); 
    $stmt->bind_param("s",$email);
    $stmt->execute();
    $stmt->bind_result($id, $username, $email);
    $stmt->fetch();
    
    $user = array(
    'id'=>$id, 
    'username'=>$username, 
    'email'=>$email,
    'Created_date'=> $date
    );
    
    $stmt->close();
    
    $response['errors'] = false; 
    $response['message'] = 'User registered successfully'; 
    $response['user'] = $user;
    echo json_encode($response); 
    }
    }
}else{
    $response['errors'] = true; 
    $response['message'] = 'check paramater invalid'; 
    echo json_encode($response); 
}


        
?>