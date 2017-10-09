<?php
$con=mysqli_connect("localhost","--------","--------","--------"); 
 
mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
} 
 
$email = $_POST['email'];  
$name = $_POST['name'];   
$password = $_POST['password'];  
$child_name = $_POST['child_name'];
   
$result = mysqli_query($con,"insert into user (email,name,pswrd,update_date,child_name) values ('$email','$name','$password',now(),'$child_name')");  
  
  if($result){  
    echo 'success';  
  }  
  else{  
    echo 'failure';  
  }  
  
  
mysqli_close($con);  
?> 
 

