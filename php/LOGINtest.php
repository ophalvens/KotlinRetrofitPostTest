<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Max-Age: 1000');
header('Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With');

define ('INDEX', true); // 'geheime' variabele om te testen in dbcon.php en base.php : daar wordt gefaald als deze ontbreken
// --- Step 0 : connect to db
require 'inc/dbcon.php';
require 'inc/base.php';

// LOGIN
// --- antwoord met de gebruikergegevens indien de combo bestaat  

// de login nakijken
// let op : we halen deze uit $postvars ipv uit $_POST, wat je online meer zal tegenkomen.
$stmt = $conn->prepare("select ID, NAME FROM gebruikers where NAME like ? and PW like ?"); 


if (!$stmt){
    //oeps, probleem met prepared statement!
    $response['code'] = 7;
    $response['status'] = 200; // 'ok' status, anders een bad request ...
    $response['data'] = $conn->error;
    deliver_response($response);
}
// bind parameters
// s staat voor string
// i staat voor integer
// d staat voor double
// b staat voor blob
// "ss" staat dus voor String, String
if(!$stmt->bind_param("ss", $_POST['name'], $_POST['password'])){
    // binden van de parameters is mislukt
    $response['code'] = 7;
    $response['status'] = 200; // 'ok' status, anders een bad request ...
    $response['data'] = $conn->error;
    deliver_response($response);
}

if (!$stmt->execute()) {
    // het uitvoeren van het statement zelf mislukte
    $response['code'] = 7;
    $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
    $response['data'] = $conn->error;
    deliver_response($response);
}

$result = $stmt->get_result();

if (!$result) {
    // er kon geen resultset worden opgehaald
    $response['code'] = 7;
    $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
    $response['data'] = $conn->error;
    deliver_response($response);
}


// De volgende 3 waarden voegen we toe om in onze clients meer detail
// te hebben over wat we doorsturen. Laat deze 3 lijnen wel niet staan
// in je productie omgeving.
$response['postvars'] = json_encode($postvars);
$response['body'] = json_encode($body);
$response['post'] = json_encode($_POST);

// Vorm de resultset om naar een structuur die we makkelijk kunnen
// doorgeven en stop deze in $response['data']
$response['data'] = getJsonObjFromResult($result); // -> fetch_all(MYSQLI_ASSOC)

// maak geheugen vrij op de server door de resultset te verwijderen
$result->free();
// sluit de connectie met de databank
$conn->close();
// Return Response to browser
deliver_response($response);
?>