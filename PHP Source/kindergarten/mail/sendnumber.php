<?php

$sendto = $_POST['sendto'];
$mailbody = $_POST['mailbody'];

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
$mail->Subject = "?œìš¸???´ë¦°?´ì§‘ ?¸ì¦ë²ˆí˜¸?…ë‹ˆ??";
$mail->Body = "$mailbody";
$mail->Send();

?>
