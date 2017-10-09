<?php

$con=mysqli_connect("localhost","--------","--------","--------");
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
   
$email = $_POST['email'];  
$password = $_POST['password'];  
  
mysqli_set_charset($con,"utf8");
   
$res = mysqli_query($con, "SELECT email,name,kinder_id,update_date,teacher_bool,ready_register,child_name
FROM user
WHERE email = '$email' AND pswrd = '$password' 
LIMIT 0,1000");

$result = array();  
   
while($row = mysqli_fetch_array($res)){  
  array_push($result,  
    array('email'=>$row[0],'name'=>$row[1],'kinder_id'=>$row[2],'update_date'=>$row[3], 'teacher_bool' =>$row[4], 'ready_register' =>$row[5],'child_name' =>$row[6]
    ));  
}  
   
echo json_encode(array("result"=>$result));  
   
mysqli_close($con);  
   
?>  
