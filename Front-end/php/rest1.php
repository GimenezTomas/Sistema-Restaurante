<?php
    function Perfil(){
        $json = file_get_contents('collectionRestaurante.json');
        $datos = json_decode($json,true);
        $ft = $datos["logo"];
        $name = $datos["nombre"];
        echo "<img class='zz' src='$ft'>";
        echo "<div class='nn'><h1 style='color: white'>$name</h1></div>";   
    }

    function Secciones(){
        $json = file_get_contents('collectionRestaurante.json');
        $datos = json_decode($json,true);
        $secc = $datos["seccionesPlatos"];
        for ($i=0; $i < count($secc) ; $i++) {
            $name = $datos["seccionesPlatos"][$i]["nombre"];
            ?><div class='platoContainer'>
                    <div class='nombreSeccion'>
                        <h1><?php echo $name ?> </h1>
                    </div>
                    <div class='body'><?php
            $platos = $datos["seccionesPlatos"][$i]["platos"];
            for ($j=0; $j < count($platos) ; $j++) { 
                $nam = $platos[$j]["nombre"];
                $img = $platos[$j]["imagen"];
                $precio = $platos[$j]["precio"];
                $demora = $platos[$j]["demora"];
                $estrella = $platos[$j]["calificacion"];
                ?>      <div class='containerCard'>
                                <div class='card'>
                                    <div class='face face1'>
                                        <div class='content'>
                                            <div class='icon'>
                                                <img src='$img' style='height: 200px; width: 300px;'>
                                            </div>
                                        </div>
                                    </div>
                                    <div class='face face2'>
                                        <div style='background-color: white; width:100%; '>
                                            <div style='margin-left: auto; margin-right: auto;'>
                                                <h3 style='text-align: center;'>
                                                    <a><?php echo ($nam); ?></a>
                                                </h3>
                                                <p style='text-align: center; font-weight: 500'>$<?php echo $precio; ?></p>
                                                <div style='text-align: center;'>
                                                    <span>
                                                        <svg style='vertical-align: center' width='1em' height='1em' viewBox='0 0 16 16' class='bi bi-star-fill' fill='currentColor' xmlns='http://www.w3.org/2000/svg'>
                                                            <path d='M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.283.95l-3.523 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z'/>
                                                        </svg>                                        <h1 style='margin-right: 3%; font-size: 15px; display: inline-flex;'><?php echo $estrella ?></h1>
                                                    </span>
                                                    <h1 style='font-size: 15px; display: inline-flex;'>Demora: <?php echo $demora?></h1>
                                                </div>
                                                <div class='boton' onclick="llamarAgregados('boton')">
                                                    <div class='backgroundSVG'>
                                                        <svg width='1em' height='1em' viewBox='0 0 16 16' class='bi bi-plus-circle plus' fill='currentColor' xmlns='http://www.w3.org/2000/svg'>
                                                            <path fill-rule='evenodd' d='M8 15A7 7 0 1 0 8 1a7 7 0 0 0 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z'/>
                                                            <path fill-rule='evenodd' d='M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z'/>
                                                        </svg>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <?php
                }   ?>
            </div>
            </div><?php
            }
    }
?>
<html>
<head>
    <title>Menu</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="rest.css">
    <script src="jquery-3.4.1.min.js"></script>
    <meta charset="UTF-8">
</head>
<body>
<header>
    <div class="cr">
        <svg onmousedown="llamarCarrito('car')" viewBox="0 0 16 16" class="bi bi-cart4 car carrito" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd" d="M0 2.5A.5.5 0 0 1 .5 2H2a.5.5 0 0 1 .485.379L2.89 4H14.5a.5.5 0 0 1 .485.621l-1.5 6A.5.5 0 0 1 13 11H4a.5.5 0 0 1-.485-.379L1.61 3H.5a.5.5 0 0 1-.5-.5zM3.14 5l.5 2H5V5H3.14zM6 5v2h2V5H6zm3 0v2h2V5H9zm3 0v2h1.36l.5-2H12zm1.11 3H12v2h.61l.5-2zM11 8H9v2h2V8zM8 8H6v2h2V8zM5 8H3.89l.5 2H5V8zm0 5a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0zm9-1a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0z"/>
        </svg>
        <span id="spanContador">
            <h6 id="contador">0</h6>
        </span>
    </div>
</header>

<div class="padre">
    <div class="nombre"><img class="zz" src="images/9z.png"></div>
</div>

<div class="res">
    <?php Perfil(); ?>
</div>
<div class="tiposBoxContainer">
    <nav class = tiposBox>
        <a class = "tipos" href = "#seccionMinutas" onmouseout="mouseoverWHITE('postresTBOX', false)" onmouseover="mouseoverWHITE('postresTBOX', true)">
            <img src="images/helado.png" alt="not found" width = "25" height = "25">
            <h3 id = "postresTBOX" class = "tipos_font">Postres</h3>
        </a>
        <a class = "tipos" href = "#22" onmouseout="mouseoverWHITE('tragosTBOX', false)" onmouseover="mouseoverWHITE('tragosTBOX', true)">
            <img src="images/empanada.png" alt="not found" width = "25" height = "25">
            <h3 id="tragosTBOX" class = "tipos_font">Tragos</h3>
        </a>
        <a class = "tipos" href = "#seccionBebidas" onmouseout="mouseoverWHITE('bebidasTBOX', false)" onmouseover="mouseoverWHITE('bebidasTBOX', true)">
            <img src="images/helado.png" alt="not found" width = "25" height = "25">
            <h3 id = "bebidasTBOX" class = "tipos_font">Bebidas</h3>
        </a>
        <a class = "tipos" href = "#seccionMinutas" onmouseout="mouseoverWHITE('minutasTBOX', false)" onmouseover="mouseoverWHITE('minutasTBOX', true)">
            <img src="images/helado.png" alt="not found" width = "25" height = "25">
            <h3 id = "minutasTBOX" class = "tipos_font">Minutas</h3>
        </a>
        <a class = "tipos" href = "#seccionEmpanadas" onmouseout="mouseoverWHITE('entradasTBOX', false)" onmouseover="mouseoverWHITE('entradasTBOX', true)">
            <img src="images/empanada.png" alt="not found" width = "25" height = "25">
            <h3 id="entradasTBOX" class = "tipos_font">Entradas</h3>
        </a>
        <a class = "tipos" href = "#seccionMinutas" onmouseout="mouseoverWHITE('postres2TBOX', false)" onmouseover="mouseoverWHITE('postres2TBOX', true)">
            <img src="images/helado.png" alt="not found" width = "25" height = "25">
            <h3 id = "postres2TBOX" class = "tipos_font">Postres</h3>
        </a>
        <a class = "tipos" href = "#22" onmouseout="mouseoverWHITE('tragos2TBOX', false)" onmouseover="mouseoverWHITE('tragos2TBOX', true)">
            <img src="images/empanada.png" alt="not found" width = "25" height = "25">
            <h3 id="tragos2TBOX" class = "tipos_font">Tragos</h3>
        </a>
    </nav>
</div>
<div id="seccionesContainer">
    <?php Secciones();?>
</div>

<div id="factura" class="containerFactura" >
    <div class="modalContent">
        <div class="headerNombrePagina">
            <svg onmousedown="activarDisplayFactura('factura', false)" viewBox="0 0 16 16" class="bi bi-arrow-left-circle-fill" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path fill-rule="evenodd" d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-4.5.5a.5.5 0 0 0 0-1H5.707l2.147-2.146a.5.5 0 1 0-.708-.708l-3 3a.5.5 0 0 0 0 .708l3 3a.5.5 0 0 0 .708-.708L5.707 8.5H11.5z"/>
            </svg>
            <img src="images/appleLogo.png" alt="">
        </div>
        <div class="headerUsuario">
            <img src="images/usuarioLogo.png" alt="">
            <h1>Tomás Giménez</h1>
            <!--<h2>PUNTOS: 0</h1> SI O NO?-->
        </div>
        <div class="headerRestaurante">
            <img src="images/logoRestaurante.jpg" alt="">
            <h1>Cantina Los Lopez</h1>
        </div>
        <div id = "pedidos" class="pedidos">
            <div style="height: 290px;">
                <div id="myModal" class="modal">
                    <div class="modal-content">
                        <span class="close closer">&times;</span>
                        <h1 style="font-size: 20px;text-align: center;margin: 11%;">¿Estas seguro que quieres vaciar la canasta?</h1>
                        <div style="display: flex; justify-content: center;">
                            <svg class="closer" onmousedown="vaciarCanasta('proximaOrdenBody')" id = "canastaVaciaFalse"  style="margin-right:30px" width="30px" height="30px" viewBox="0 0 16 16" class="bi bi-check-circle-fill" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                <path fill-rule="evenodd" d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z"/>
                            </svg>
                            <svg class="closer" id = "canastaVaciaTrue" margin-left="10px" width="30px" height="30px" viewBox="0 0 16 16" class="bi bi-x-circle-fill" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                <path fill-rule="evenodd" d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.354 4.646a.5.5 0 1 0-.708.708L7.293 8l-2.647 2.646a.5.5 0 0 0 .708.708L8 8.707l2.646 2.647a.5.5 0 0 0 .708-.708L8.707 8l2.647-2.646a.5.5 0 0 0-.708-.708L8 7.293 5.354 4.646z"/>
                            </svg>
                        </div>
                    </div>
                </div>
                <ul id="platos">
                    <div class="platosPedidos">
                        <div class="header">
                            <h1>Platos ordenados</h1>
                        </div>
                        <div class="body">
                            <div id="platosYaPedidos" class="platoss">
                                <li id = "plato2" class="pedido">
                                    <img src="images/ñoquis.jpg" alt="">
                                    <h3>Ñoquis con salsa</h3>
                                    <div class="p">
                                        <h2>$</h2>
                                        <h4 class="precio">590</h4>
                                    </div>
                                </li>
                                <li id = "plato3" class="pedido">
                                    <img src="images/reyes.jpg" alt="">
                                    <h3>Milanesa con papas fritas</h3>
                                    <div class="p">
                                        <h2>$</h2>
                                        <h4 class="precio">490</h4>
                                    </div>
                                </li>
                                <li id = "plato4" class="pedido">
                                    <img src="images/ñoquis.jpg" alt="">
                                    <h3>Ñoquis con salsa</h3>
                                    <div class="p">
                                        <h2>$</h2>
                                        <h4 class="precio">590</h4>
                                    </div>
                                </li>
                                <li id = "plato5" class="pedido">
                                    <img src="images/reyes.jpg" alt="">
                                    <h3>Milanesa con papas fritas</h3>
                                    <div class="p">
                                        <h2>$</h2>
                                        <h4 class="precio">490</h4>
                                    </div>
                                </li>
                                <li id = "plato6" class="pedido">
                                    <img src="images/ñoquis.jpg" alt="">
                                    <h3>Ñoquis con salsa</h3>
                                    <div class="p">
                                        <h2>$</h2>
                                        <h4 class="precio">590</h4>
                                    </div>
                                </li>

                                <li id = "plato7" class="pedido">
                                    <img src="images/ñoquis.jpg" alt="">
                                    <h3>Ñoquis con salsa</h3>
                                    <div class="p">
                                        <h2>$</h2>
                                        <h4 class="precio">590</h4>
                                    </div>
                                </li>
                                <li id = "plato8" class="pedido">
                                    <img src="images/ñoquis.jpg" alt="">
                                    <h3>Ñoquis con salsa</h3>
                                    <div class="p">
                                        <h2>$</h2>
                                        <h4 class="precio">590</h4>
                                    </div>
                                </li>
                            </div>
                            <div id="precioPlatosYAPEDIDOS" class="precio" style="margin-bottom: 15px; margin-top: 15px">
                                <h1 style="display: contents; font-size: 20px; color:white">Total: $</h1>
                                <span>
                                    <h2 style="font-size: 20px; color:white" id="precioActualizadoYP">500</h2>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="proximaOrden">
                        <div class="header">
                            <h1>Proxima orden</h1>
                        </div>
                        <div id="proximaOrdenBody" class="body">
                            <!--<li id = "plato1" class="pedido" onmouseover="botonEliminar('botonDelete1', true)" onmouseout="botonEliminar('botonDelete1', false)">
                                <img src="images/reyes.jpg" alt="">
                                <h3>Milanesa con papas fritas</h3>
                                <div class="p">
                                    <h2>$</h2>
                                    <h4 class="precio">490</h4>
                                    <svg id="botonDelete1" onmousedown="eliminarPlato('plato1')" viewBox="0 0 16 16" class="bi bi-x-circle-fill" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                        <path fill-rule="evenodd" d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.354 4.646a.5.5 0 1 0-.708.708L7.293 8l-2.647 2.646a.5.5 0 0 0 .708.708L8 8.707l2.646 2.647a.5.5 0 0 0 .708-.708L8.707 8l2.647-2.646a.5.5 0 0 0-.708-.708L8 7.293 5.354 4.646z"/>
                                    </svg>
                                </div>
                            </li>-->
                        </div>
                        <div id="carritoVacio2" style="display: block;">
                            <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-cart4" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                <path fill-rule="evenodd" d="M0 2.5A.5.5 0 0 1 .5 2H2a.5.5 0 0 1 .485.379L2.89 4H14.5a.5.5 0 0 1 .485.621l-1.5 6A.5.5 0 0 1 13 11H4a.5.5 0 0 1-.485-.379L1.61 3H.5a.5.5 0 0 1-.5-.5zM3.14 5l.5 2H5V5H3.14zM6 5v2h2V5H6zm3 0v2h2V5H9zm3 0v2h1.36l.5-2H12zm1.11 3H12v2h.61l.5-2zM11 8H9v2h2V8zM8 8H6v2h2V8zM5 8H3.89l.5 2H5V8zm0 5a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0zm9-1a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0z"/>
                            </svg>
                            <h1>La orden esta vacia</h1>
                        </div>
                    </div>
                </ul>
            </div>
            <div class="pedir">
                <button id="botonPedir" onclick=pedir()>Pedir  ($1500)</button>
                <div>
                    <svg onmousedown="vaciarCanastaModal('proximaOrdenBody')" viewBox="0 0 16 16" class="bi bi-trash" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                        <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
                        <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4L4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
                    </svg>
                </div>
            </div>
        </div>
        <div id="carritoVacio" style="display: none;">
            <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-cart4" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path fill-rule="evenodd" d="M0 2.5A.5.5 0 0 1 .5 2H2a.5.5 0 0 1 .485.379L2.89 4H14.5a.5.5 0 0 1 .485.621l-1.5 6A.5.5 0 0 1 13 11H4a.5.5 0 0 1-.485-.379L1.61 3H.5a.5.5 0 0 1-.5-.5zM3.14 5l.5 2H5V5H3.14zM6 5v2h2V5H6zm3 0v2h2V5H9zm3 0v2h1.36l.5-2H12zm1.11 3H12v2h.61l.5-2zM11 8H9v2h2V8zM8 8H6v2h2V8zM5 8H3.89l.5 2H5V8zm0 5a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0zm9-1a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0z"/>
            </svg>
            <h1>El carrito esta vacio</h1>
        </div>
    </div>
</div>
<div id = "containerAgregados" class="containerAgregados">
    <div class="mContent">
        <div class="cardAgregados">
            <div class="left">
                <img id="imagenAG" src="images/ñoquis.jpg" alt="Card image cap">
            </div>
            <div class="right">
                <div class="borrar">
                    <svg onclick = cerrarAgregados()  width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-arrow-right-circle-fill" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                        <path fill-rule="evenodd" d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-11.5.5a.5.5 0 0 1 0-1h5.793L8.146 5.354a.5.5 0 1 1 .708-.708l3 3a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708-.708L10.293 8.5H4.5z"/>
                    </svg>
                </div>
                <div id="nombreAG"class="nombre">
                    <h3>Ñoquis con crema y cebolla de verdeo</h3>
                    <p>Los ñoquis son un tipo de pasta italiana. Se elaboran con patatas, harina y queso de ricota. Una variedad muy conocida en las regiones de Friuli-Venecia Julia y Trentino-Alto Adigio y denominada gnocchi di pane se hace con pan rallado</p>
                </div>
                <div id="opciones">
                    <div class = "tiposSeccion">
                        <div class="header">
                            <h1>Salsas</h1>
                            <div class="obligatorio">
                                <h1>Obligatorio!</h1>
                                <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-exclamation-circle" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                    <path fill-rule="evenodd" d="M8 15A7 7 0 1 0 8 1a7 7 0 0 0 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
                                    <path d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 4.995z"/>
                                </svg>
                            </div>
                        </div>
                        <ul id="salsas" class="body indispensable">
                            <li id="botonSalsa1" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Fileto</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="botonSalsa2" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Crema</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="botonSalsa3" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Boloñesa</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="botonSalsa4" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Salsa Rosa</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="botonSalsa5" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Salsa Blanca</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="botonSalsa6" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Fileto</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="botonSalsa7" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Fileto</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="botonSalsa8" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Fileto</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="botonSalsa9" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Fileto</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class = "tiposSeccion">
                        <h1 class="header">Postres</h1>
                        <ul id="postres" class="body">
                            <li id="btnS1" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Mousse de chocolate</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="btnS2" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Mousse de limón</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="btnS3" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Flan con dulce de leche</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="btnS4" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Budin</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="btnS5" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Chocotorta</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="btnS6" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Ensalada de frutas</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="btnS7" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Helado de chocolate</h3>
                                    <h2 class = "precioAgregado">(+$45)</h2>
                                </div>
                            </li>
                            <li id="btnS8" class="botonMasAgregado" onclick=calcularPrecio()>
                                <input type="checkbox"></input>
                                <div>
                                    <h3>Helado de crema</h3>
                                    <h2 class = "precioAgregado">(+$120.75)</h2>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="precioTotal">
                        <h1>Total: $</h1>
                        <span>
                                <h2 id="precioActualizado">500</h2>
                                <h3 id="precioBase">$400</h3>
                            </span>
                    </div>
                </div>
                <section class="padre">
                    <textarea placeholder = "Aclaraciones..."name="" id="" class="aclaraciones"></textarea>
                    <button onmousedown="addPlatoAlCarrito('nombreAG', 'precioActualizado','imagenAG', 'sxaxax')" class="botonAgregar">Agregar</button>
                </section>
            </div>
        </div>
    </div>
</div>
<script>
    //contarPlatos()

    function clearArray(array) {
        while (array.length) {
            array.pop();
        }
    }
    /*clases*/
    class Plato {
        constructor(nombre, precio, descripcion, img, agregados){
            this.nombre = nombre
            this.precio = precio
            this.descripcion = descripcion
            this.img = img
            this.agregados = agregados
        }
    }

    platos=[]
    platosYaPedidos=[]
    function confirmarSeccionesIndispensables(id){
        let opciones = document.getElementById(id)
        let enviarOK = false
        for(let i=0; i<opciones.childNodes.length; i++) {
            if (opciones.childNodes[i].tagName=="DIV" && opciones.childNodes[i].classList.contains("tiposSeccion")){
                for (let j = 0; j < opciones.childNodes[i].childNodes.length; j++) {
                    if (opciones.childNodes[i].childNodes[j].tagName === 'UL' && opciones.childNodes[i].childNodes[j].classList.contains("indispensable")){
                        let seccion = opciones.childNodes[i].childNodes[j]
                        for (k = 0; k <seccion.childNodes.length ; k++) {
                            if (seccion.childNodes[k].tagName === 'LI'){
                                for (let l = 0; l < seccion.childNodes[k].childNodes.length ; l++) {
                                    if (seccion.childNodes[k].childNodes[l].tagName === 'INPUT' && seccion.childNodes[k].childNodes[l].checked){
                                        enviarOK = true
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return enviarOK
    }
    function addPlatoAlCarrito(idnombre, idprecio, idimg, agregados){
        if (confirmarSeccionesIndispensables('opciones')){
            let nombre = document.getElementById(idnombre).getElementsByTagName("H3")[0].innerHTML
            let descripcion = document.getElementById(idnombre).getElementsByTagName("P")[0].innerHTML
            let img = document.getElementById(idimg).src
            let precio = document.getElementById(idprecio).innerHTML

            let plato = new Plato(nombre, precio, descripcion, img, agregados)

            platos.push(plato)
            let carrito = document.getElementById('proximaOrdenBody')

            let s = 'plato'+(carrito.getElementsByTagName("LI").length+1)
            let as = 'botonDelete'+(carrito.getElementsByTagName("LI").length+1)

            let image = document.createElement("IMG")
            image.src=img;

            let h3 = document.createElement("H3")
            h3.innerHTML = nombre

            let div = document.createElement("DIV")
            div.classList.add("p")

            let h2 = document.createElement("H2")
            h2.innerHTML = "$"

            let h4 = document.createElement("H4")
            h4.classList.add("precio")
            h4.innerHTML = precio

            let svg = document.createElement("SVG")
            svg.setAttribute("id", as)
            svg.addEventListener("mousedown", function(){eliminarPlato(s)})
            svg.setAttribute("viewBox", "0 0 16 16")
            svg.classList.add("bi", "bi-x-circle-fill")
            svg.setAttribute("fill", "currentColor")
            svg.setAttribute("xlmns", "http://www.w3.org/2000/svg")

            let path = document.createElement("PATH")
            path.setAttribute("fill-rule", "evenodd")
            path.setAttribute("d", "M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.354 4.646a.5.5 0 1 0-.708.708L7.293 8l-2.647 2.646a.5.5 0 0 0 .708.708L8 8.707l2.646 2.647a.5.5 0 0 0 .708-.708L8.707 8l2.647-2.646a.5.5 0 0 0-.708-.708L8 7.293 5.354 4.646z")

            svg.appendChild(path)

            div.appendChild(h2)
            div.appendChild(h4)
            div.appendChild(svg)

            let node = document.createElement("LI")
            node.classList.add("pedido")
            node.addEventListener("mouseover", function(){botonEliminar(as, true)})
            node.addEventListener("mouseout", function(){botonEliminar(as, false)})
            node.setAttribute("id", "plato"+(carrito.getElementsByTagName("LI").length+1))

            node.appendChild(image)
            node.appendChild(h3)
            node.appendChild(div)

            carrito.appendChild(node)

            actualizarPrecioTotal('proximaOrdenBody', 'precio', 'botonPedir')
            for (let i = 0; i < document.getElementsByClassName('obligatorio').length; i++) {
                document.getElementsByClassName('obligatorio')[i].style.display="none"
            }
            contarPlatos()
            cerrarAgregados()
        }else{
            for (let i = 0; i < document.getElementsByClassName('obligatorio').length; i++) {
                document.getElementsByClassName('obligatorio')[i].style.display="block"
            }
        }
    }

    /*modal agregados*/
    function llamarAgregados(claseIcono){
        var modal = document.getElementById("containerAgregados");

        // Get the <span> element that closes the modal
        var span = document.getElementsByClassName(claseIcono)[0];

        // When the user clicks the button, open the modal
        modal.style.display = "block";

        // When the user clicks on <span> (x), close the modal
        span[0].onclick = function() {
            modal.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
    }

    calcularPrecio()

    function calcularPrecio(){
        let precio = parseFloat((document.getElementById("precioBase").innerHTML).substr(1))
        let opciones = document.getElementById('opciones')
        for(let i=0; i<opciones.childNodes.length; i++){
            if( opciones.childNodes[i].tagName=="DIV" && opciones.childNodes[i].className!="precioTotal"){
                let tipoAgregado = opciones.childNodes[i]
                for(let j=0; j<tipoAgregado.childNodes.length; j++){
                    if(tipoAgregado.childNodes[j].tagName=="UL"){
                        let extras = tipoAgregado.childNodes[j]
                        for(let w = 0; w < extras.childNodes.length; w++){
                            if(extras.childNodes[w].tagName=="LI"){
                                let extra = extras.childNodes[w]
                                let ok=false
                                for(let q=0; q<extra.childNodes.length ;q++){
                                    if(extra.childNodes[q].tagName=="INPUT"){
                                        if(extra.childNodes[q].checked){
                                            ok = true
                                        }
                                    }
                                    else if(ok && extra.childNodes[q].tagName=="DIV"){
                                        for(let z = 0; z < extra.childNodes[q].childNodes.length; z++){
                                            if(extra.childNodes[q].childNodes[z].tagName=="H2"){
                                                let precioADD = ""
                                                parseFloat(precioADD)
                                                for(let y=0; y<extra.childNodes[q].childNodes[z].innerHTML.length; y++){
                                                    if(extra.childNodes[q].childNodes[z].innerHTML.charCodeAt(y)<58 && extra.childNodes[q].childNodes[z].innerHTML.charCodeAt(y)>47){
                                                        precioADD = precioADD + extra.childNodes[q].childNodes[z].innerHTML[y]
                                                    }
                                                }
                                                precio += Number(precioADD)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        document.getElementById("precioActualizado").innerHTML = precio
    }
    function cerrarAgregados(){
        document.getElementById("containerAgregados").style.display = "none"
    }
    /*factura*/

    actualizarPrecioTotal('proximaOrdenBody', 'precio', 'botonPedir')

    function activarDisplayFactura(id, display){
        if(display){
            document.getElementById(id).style.display="block"
        }
        else{
            document.getElementById(id).style.display="none"
        }
    }
    function botonEliminar(id,display){
        if(display){
            document.getElementById(id).style.display="block"
        }
        else{
            document.getElementById(id).style.display="none"
        }
    }

    function eliminarPlato(idEliminar){
        let plato = document.getElementById(idEliminar)
        plato.parentNode.removeChild(plato)
        for (let i = 0; i < platos.length; i++) {
            if ((i+1).toString() == idEliminar.substr(idEliminar.length-1)){
                platos.splice(i)
                break
            }
        }
        contarPlatos()
        actualizarPrecioTotal('proximaOrdenBody', 'precio', 'botonPedir')
        llamarCarrito('car')
    }

    function vaciarCanasta(id){
        let platosHTML = document.getElementById(id)
        for(let plato = 0;  plato<platosHTML.childNodes.length; plato++){
            platosHTML.removeChild(platosHTML.childNodes[plato])
            plato--
        }
        clearArray(platos)
        document.getElementById("myModal").style.display="none"
        actualizarPrecioTotal('proximaOrdenBody', 'precio', 'botonPedir')
        contarPlatos()
        llamarCarrito('car')
    }

    function actualizarPrecioTotal(idPlatos, classNamePrecio, clasePedir){
        let platos = document.getElementById(idPlatos)
        let precio = 0
        for(let plato = 0;  plato<platos.childNodes.length; plato++){
            if(platos.childNodes[plato].childNodes.length>0){
                for(let x = 0; x < platos.childNodes[plato].childNodes.length; x++){
                    if(platos.childNodes[plato].childNodes[x].tagName=="DIV"){
                        for(let w=0; w<platos.childNodes[plato].childNodes[x].childNodes.length; w++){
                            if(platos.childNodes[plato].childNodes[x].childNodes[w].className == classNamePrecio){
                                precio = precio + Number(platos.childNodes[plato].childNodes[x].childNodes[w].innerHTML)
                                break
                            }
                        }
                    }
                }
            }
        }
        document.getElementById(clasePedir).innerHTML = "Pedir ($"+precio+")"
        if(precio==0){
            canastaVacia(true)
        }
    }

    function actualizarPrecioPlatosPedidos(){
        let platos = document.getElementById("platosYaPedidos")
        let precio = 0
        for(let plato = 0;  plato<platos.childNodes.length; plato++){
            if(platos.childNodes[plato].childNodes.length>0){
                for(let x = 0; x < platos.childNodes[plato].childNodes.length; x++){
                    if(platos.childNodes[plato].childNodes[x].tagName=="DIV"){
                        for(let w=0; w<platos.childNodes[plato].childNodes[x].childNodes.length; w++){
                            if(platos.childNodes[plato].childNodes[x].childNodes[w].className == "precio"){
                                precio = precio + Number(platos.childNodes[plato].childNodes[x].childNodes[w].innerHTML)
                                break
                            }
                        }
                    }
                }
            }
        }
        document.getElementById('precioActualizadoYP').innerHTML = precio.toString()
        if(precio==0){
            canastaVacia(true)
        }
    }

    function canastaVacia(ok){
        if(ok && platosYaPedidos.length>0){
            document.getElementById("pedidos").style.display="none"
            document.getElementById("carritoVacio").style.display="flex"
        }else{
            document.getElementById("pedidos").style.display="block"
            document.getElementById("carritoVacio").style.display="none"
        }
    }

    function canastaVacia2(ok){
        if(ok){
            document.getElementById("pedidos").style.display="block"
            document.getElementById("carritoVacio2").style.display="block"
            document.getElementById("botonPedir").innerHTML = "Pedir ($"+0+")"
            contarPlatos()
        }else{
            document.getElementById("proximaOrdenBody").style.display="block"
            document.getElementById("carritoVacio2").style.display="none"
        }
    }
    /*modal vaciar canasta*/
    function vaciarCanastaModal(){
        var modal = document.getElementById("myModal");

        // Get the <span> element that closes the modal
        var span = [document.getElementsByClassName("closer")[0], document.getElementsByClassName("closer")[2]];

        // When the user clicks the button, open the modal
        modal.style.display = "block";

        // When the user clicks on <span> (x), close the modal
        span[0].onclick = function() {
            modal.style.display = "none";
        }

        span[1].onclick = function() {
            modal.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }

    }

    function addPlatoYaPedidos(nombre, precio, img, agregados){

        let carrito = document.getElementById('platosYaPedidos')

        let s = 'plato'+(carrito.getElementsByTagName("LI").length+1)

        let image = document.createElement("IMG")
        image.src=img;

        let h3 = document.createElement("H3")
        h3.innerHTML = nombre

        let div = document.createElement("DIV")
        div.classList.add("p")

        let h2 = document.createElement("H2")
        h2.innerHTML = "$"

        let h4 = document.createElement("H4")
        h4.classList.add("precio")
        h4.innerHTML = precio

        div.appendChild(h2)
        div.appendChild(h4)

        let node = document.createElement("LI")
        node.classList.add("pedido")
        node.setAttribute("id", "platoYaPedidos"+(carrito.getElementsByTagName("LI").length+1))

        node.appendChild(image)
        node.appendChild(h3)
        node.appendChild(div)

        carrito.appendChild(node)

        actualizarPrecioPlatosPedidos()
    }

    function pedir(){
        platos.forEach(plato => addPlatoYaPedidos(plato.nombre, plato.precio, plato.img, plato.agregados))
        clearArray(document.getElementById("proximaOrdenBody"))
        for (let i = 0; i < document.getElementById("proximaOrdenBody").childNodes.length; i++) {
            if (document.getElementById("proximaOrdenBody").childNodes[i] != undefined){
                document.getElementById("proximaOrdenBody").childNodes[i].remove()
                i--
            }
        }
        platosYaPedidos.push(platos)
        clearArray(platos)
        llamarCarrito('car')
        actualizarPrecioTotal('proximaOrdenBody', 'precio', 'botonPedir')
    }

    function llamarCarrito(claseIcono){
        actualizarPrecioPlatosPedidos()
        if(platos.length==0 && platosYaPedidos.length==0){
            canastaVacia(true)
        }else if (platos.length==0 && platosYaPedidos.length>0){
            canastaVacia(false)
            canastaVacia2(true)
        } else{
            canastaVacia(false)
            canastaVacia2(false)
        }

        var modal = document.getElementById("factura");

        // Get the <span> element that closes the modal
        var span = document.getElementsByClassName(claseIcono)[0];

        // When the user clicks the button, open the modal
        modal.style.display = "block";

        // When the user clicks on <span> (x), close the modal
        span[0].onclick = function() {
            modal.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
    }
    /*tipos box*/
    function mouseoverWHITE(id, ok){
        if (ok){
            document.getElementById(id).style.color="white"
        }else{
            document.getElementById(id).style.color="#282828"
        }
    }
    /*contador*/
    function contarPlatos(){
        document.getElementById("contador").innerHTML = platos.length.toString()
        if (platos.length==0){
            document.getElementById("spanContador").style.display = 'none'
        }else{
            document.getElementById("spanContador").style.display = 'block'
        }
    }
</script>

</body>
</html>

<!--
    ERRORES:
        1) Corregir canasta vacia, anda pero hay veces que sale y no deberia
        2) SVG CRUZ del carrito no aparece en los platos agregados con JS, existen pero no se muestran
        3) VACIAR CANASTA
        4) CARRITOVACIO2
-->
<!--
    A Agregar:
        1) Carrito:
            a) Ver detalle plato
-->