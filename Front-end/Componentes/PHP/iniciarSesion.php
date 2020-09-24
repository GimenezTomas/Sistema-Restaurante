<?php
    $usuario = [
        'email' => 'Alumno21.GIMENEZ.Tomas@ipm.edu.ar',
        'password' => 'hola123',
    ];

    echo json_encode($usuario);
    
    $usuario1 = json_decode(file_get_contents('../JSON/users.json'));

    echo $usuario1[0]
?>