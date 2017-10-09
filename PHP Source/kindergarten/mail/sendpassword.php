<?php
$sendto = $_POST['sendto'];

$con = mysqli_connect("localhost","bluecarnival","sa!@22996065","bluecarnival");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$res = mysqli_query($con, "
SELECT pswrd
FROM user
WHERE email = \"$sendto\";
");

$row = mysqli_fetch_array($res);
$password = $row[0];

mysqli_close($con);

require 'PHPMailer/PHPMailerAutoload.php';

$mail = new PHPMailer();

$mail->CharSet = 'UTF-8';
$mail->IsSMTP();

$mail->Host='smtp.gmail.com';

$mail->SMTPDebug = 2;
$mail->SMTPAuth = true;
$mail->Port= 587;
$mail->SMTPSecure = 'tls';
$mail->SMTPKeepAlive = true;

$mail->Username='--------------';
$mail->Password='--------------';

$mail->From='--------------';

$mail->AddAddress($sendto);

$mail->isHTML(true);
$mail->Subject = "?œìš¸???´ë¦°?´ì§‘ ë¹„ë?ë²ˆí˜¸?…ë‹ˆ??";
$mail->Body = "ë¹„ë?ë²ˆí˜¸??$password ?…ë‹ˆ??";
$mail->Send();

?>
