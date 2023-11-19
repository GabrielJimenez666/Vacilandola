package com.example.web;

import com.example.Objetos.Bolsillo;
import com.example.Objetos.Notificacion;
import com.example.Objetos.Transaccion;
import com.example.Objetos.Usuario;
import com.example.domain.Bolsillos;
import com.example.domain.Notificaciones;
import com.example.domain.Transacciones;
import com.example.domain.Usuarios;
import com.example.servicio.UsuarioService;
import com.example.servicio.BolsilloService;
import com.example.servicio.NotificacionService;
import com.example.servicio.TransaccionService;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BolsilloService bolsilloService;

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private NotificacionService notificacionService;

    public Usuario user;
    public boolean equivocacion = false;
    public boolean equivocacion2 = false;
    public boolean contramala = false;
    public boolean contramala2 = false;
    public boolean repetido = false;
    public List<Usuarios> usuarios2;
    public List<Bolsillos> bolsillos2;
    public List<Transacciones> transacciones2;
    public List<Notificaciones> notificaciones2;
    public String fecha_actual;
    public String saldo_disponible;

    @GetMapping("/")
    public String inicio(Model model) {
        usuarios2 = usuarioService.listarUsuarios();
        bolsillos2 = bolsilloService.listarBolsillos();
        transacciones2 = transaccionService.listarTransacciones();
        notificaciones2 = notificacionService.listarNotificaciones();
        log.info("Ohayo. Ejecutando el controlador Spring MVC");
        return "inicio";
    }

    public void actualizar_datos() {
        usuarios2 = usuarioService.listarUsuarios();
        bolsillos2 = bolsilloService.listarBolsillos();
        transacciones2 = transaccionService.listarTransacciones();
        notificaciones2 = notificacionService.listarNotificaciones();
    }

    @GetMapping("/regresar_inicio")
    public String regresar_inicio() {
        return "redirect:/";
    }

    @GetMapping("/regresar_menu")
    public String regresar_menu(Model model) {
        fecha_actual = LocalDate.now().toString();
        model.addAttribute("fecha_actual", fecha_actual);
        model.addAttribute("user", user);
        actualizar_datos();
        return "menu";
    }

    @GetMapping("ir_ajustes")
    public String ir_ajustes() {
        return "ajustes";
    }

    @GetMapping("regresar_ajustes")
    public String regresar_ajustes() {
        return "ajustes";
    }

    @PostMapping("/borrar_cuenta")
    public String borrar_cuenta(Model model, @RequestParam("input_contrasena") String input_contrasena,
            @RequestParam("input_contrasena2") String input_contrasena2) {
        if (!input_contrasena.equals(input_contrasena2)) {
            equivocacion2 = true;
            return "redirect:/ir_eliminar_cuenta";
        } else {
            equivocacion2 = false;
        }
        if (!input_contrasena.equals(user.getContrasena())) {
            contramala = true;
            return "redirect:/ir_eliminar_cuenta";
        } else {
            contramala = false;
        }
        for (Usuarios usuario : usuarios2) {
            if (usuario.getId() == user.getId()) {
                usuario = usuarioService.encontrarUsuario(usuario);
                for (Bolsillos bolsillo : bolsillos2) {
                    if (bolsillo.getUsuario_id() == usuario.getId()) {
                        bolsillo = bolsilloService.encontrarBolsillo(bolsillo);
                        bolsilloService.eliminar(bolsillo);
                    }
                }
                for (Bolsillo bolsillo : user.getBolsillos()) {
                    if (bolsillo.getId() == user.getId()) {
                        user.eliminar_bolsillo(bolsillo);
                    }
                }
                usuarioService.eliminar(usuario);
                contramala = false;
                equivocacion2 = false;
                return "redirect:/";
            }
        }
        return "redirect:/ir_ajustes";
    }

    @GetMapping("/ir_eliminar_cuenta")
    public String ir_eliminar_cuenta(Model model) {
        String errado = "";
        if (equivocacion2 == false && contramala == false) {
            errado = "";
        } else {
            if (equivocacion2 == true) {
                errado = "Las contrasenas ingresadas no son iguales";
            } else {
                if (contramala == true) {
                    errado = "Contrasena incorrecta";
                }
            }
        }
        model.addAttribute("errado", errado);
        return "eliminar_cuenta";
    }

    @GetMapping("/ir_info")
    public String ir_info(Model model) {
        model.addAttribute("user", user);
        return "info";
    }

    @GetMapping("/regresar_info")
    public String regresar_info(Model model) {
        model.addAttribute("user", user);
        return "info";
    }

    @GetMapping("/regresar_bolsillos")
    public String regresar_bolsillos(Model model) {
        model.addAttribute("user", user);
        return "bolsillos";
    }

    @GetMapping("/login")
    public String login(Model model) {
        String errado;
        if (equivocacion == false) {
            errado = "";
        } else {
            errado = "Usuario o contrasena incorrecto\nIntente nuevamente";
        }
        model.addAttribute("errado", errado);
        return "login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        String advertencia;
        if (repetido == false) {
            advertencia = "";
        } else {
            advertencia = "Usuario ya existente. Elija otro usuario";
        }
        model.addAttribute("advertencia", advertencia);
        return "registro";
    }

    @PostMapping("/registrarse")
    public String registrarse(@RequestParam("input_usuario") String input_usuario,
            @RequestParam("input_contrasena") String input_contrasena,
            @RequestParam("input_email") String input_email) {

        for (Usuarios usuario : usuarios2) {
            if (usuario.getUsuario().equals(input_usuario)) {
                repetido = true;
                return "redirect:/registro";
            }
        }
        Usuarios usuario = new Usuarios();
        usuario.setUsuario(input_usuario);
        usuario.setContrasena(input_contrasena);
        usuario.setEmail(input_email);
        usuario.setSaldo(0);
        usuarioService.guardar(usuario);
        return "redirect:/";
    }

    @GetMapping("/bolsillos")
    public String verBolsillos(Model model) {
        model.addAttribute("user", user);
        return "bolsillos";
    }

    @GetMapping("/agregar_bolsillo")
    public String agregar(Bolsillos bolsillo) {
        // este metodo me cambia de pagina web (de html)
        return "modificar";
    }

    @PostMapping("/guardar_bolsillo")
    public String guardar(@RequestParam("nombre_bolsillo") String nombre, Model model) {
        // if nombre esta vacio devuelvo a enviar a la pagina de modificar con una
        // advertencia
        Bolsillos bolsillo = new Bolsillos();
        bolsillo.setUsuario_id(user.getId());
        bolsillo.setNombre(nombre);
        bolsillo.setSaldo(0);
        bolsilloService.guardar(bolsillo);
        bolsillos2.add(bolsillo);
        Bolsillo bolsillo2 = new Bolsillo(bolsillo.getId(), bolsillo.getUsuario_id(), bolsillo.getNombre(),
                bolsillo.getSaldo());
        user.agregar_bolsillo(bolsillo2);
        model.addAttribute("user", user);
        fecha_actual = LocalDate.now().toString();
        model.addAttribute("fecha_actual", fecha_actual);
        actualizar_datos();
        return "menu";
    }

    @GetMapping("/eliminar_bolsillo/{id}")
    public String eliminar(Bolsillos bolsillo, Model model) {
        for (Bolsillo bol : user.getBolsillos()) {
            if (bol.getId() == bolsillo.getId()) {
                user.setSaldo(user.getSaldo() + bol.getSaldo());
                user.eliminar_bolsillo(bol);
                break;
            }
        }
        for (Usuarios user2 : usuarios2) {
            if (user.getId() == user2.getId()) {
                user2 = usuarioService.encontrarUsuario(user2);
                user2.setSaldo(user.getSaldo());
                usuarioService.actualizar(user2);
            }
        }
        bolsilloService.eliminar(bolsillo);
        bolsillos2.remove(bolsillo);
        fecha_actual = LocalDate.now().toString();
        model.addAttribute("fecha_actual", fecha_actual);
        model.addAttribute("user", user);
        actualizar_datos();
        return "menu";
    }

    // vacia el saldo del bolsillo y pasa todo al saldo principal de la cuenta
    @GetMapping("/vaciar_bolsillo/{id}")
    public String vaciar(Model model, Bolsillos bolsillo) {
        // se actualiza el saldo principal de la cuenta y el saldo del bolsillo queda en
        // 0
        for (Bolsillo bol : user.getBolsillos()) {
            if (bol.getId() == bolsillo.getId()) {
                user.setSaldo(user.getSaldo() + bol.getSaldo());
                bol.setSaldo(0);
                break;
            }
        }
        // se actualiza el nuevo saldo del bolsillo en la base de datos
        bolsillo = bolsilloService.encontrarBolsillo(bolsillo);
        bolsillo.setSaldo(0);
        bolsilloService.guardar(bolsillo);
        // se actualiza el nuevo saldo principal del usuario en la base de datos
        for (Usuarios user2 : usuarios2) {
            if (user.getId() == user2.getId()) {
                user2 = usuarioService.encontrarUsuario(user2);
                user2.setSaldo(user.getSaldo());
                usuarioService.actualizar(user2);
            }
        }
        model.addAttribute("user", user);
        actualizar_datos();
        return "bolsillos";
    }

    @GetMapping("/mover_dinero")
    public String mover_dinero(Model model) {
        model.addAttribute("user", user);
        String advertencia = "";
        model.addAttribute("advertencia", advertencia);
        return "bol_mover_dinero";
    }

    @PostMapping("mover_dinero_bolsillos")
    public String mover_dinero_bolsillos(Model model, @RequestParam("seleccionDeBolsillo") int bolId1,
            @RequestParam("seleccionDeBolsillo2") int bolId2, @RequestParam("monto") double monto) {
        System.out.println("PRIMER bolsillo escogido: " + bolId1);
        System.out.println("SEGUNDO bolsillo escogido: " + bolId2);
        if (0 > (monto)) {
            String advertencia = "El monto no puede ser negativo";
            model.addAttribute("advertencia", advertencia);
            model.addAttribute("user", user);
            return "bol_mover_dinero";
        }
        // se verifica que no se haya escogido el mismo bolsillo
        if (bolId1 == bolId2) {
            String advertencia = "No puede seleccionar el mismo bolsillo";
            model.addAttribute("advertencia", advertencia);
            model.addAttribute("user", user);
            return "bol_mover_dinero";
        }
        // se verifica que el monto no sobrepase el saldo del bolsillo donde se va a
        // retirar la plata
        for (Bolsillos bol : bolsillos2) {
            if (bol.getId() == bolId1) {
                if (bol.getSaldo() < monto) {
                    String advertencia = "El monto que desea mover supera al saldo disponible en el bolsillo: "
                            + bol.getNombre();
                    model.addAttribute("advertencia", advertencia);
                    model.addAttribute("user", user);
                    return "bol_mover_dinero";
                }
            }
        }
        // se modifica el bolsillo del que se saca la plata en la base de datos
        for (Bolsillos bol : bolsillos2) {
            if (bol.getId() == bolId1) {
                bol = bolsilloService.encontrarBolsillo(bol);
                bol.setSaldo(bol.getSaldo() - monto);
                bolsilloService.guardar(bol);
            }
        }
        // se modifica el bolsillo del que se saca la plata en la instancia de Usuario
        // (user)
        for (Bolsillo bol : user.getBolsillos()) {
            if (bol.getId() == bolId1) {
                bol.setSaldo(bol.getSaldo() - monto);
            }
        }
        // se agrega el monto al saldo del bolsillo en el que se recibe el dinero y se
        // actualiza en la base de datos
        for (Bolsillos bol : bolsillos2) {
            if (bol.getId() == bolId2) {
                bol = bolsilloService.encontrarBolsillo(bol);
                bol.setSaldo(bol.getSaldo() + monto);
                bolsilloService.guardar(bol);
            }
        }
        for (Bolsillo bol : user.getBolsillos()) {
            if (bol.getId() == bolId2) {
                bol.setSaldo(bol.getSaldo() + monto);
            }
        }
        model.addAttribute("user", user);
        actualizar_datos();
        return "bolsillos";
    }

    @GetMapping("/agregar_dinero_bol")
    public String agregar_dinero_bol(Model model) {
        String advertencia = "";
        model.addAttribute("advertencia", advertencia);
        model.addAttribute("user", user);
        return "agregar_dinero_bol";
    }

    @PostMapping("/agregar_dinero_bolsillo")
    public String agregar_dinero_bolsillo(@RequestParam("seleccionDeBolsillo") int bolId,
            @RequestParam("monto") double monto, Model model) {
        if ((monto) < 0) {
            String advertencia = "El monto no puede ser negativo";
            model.addAttribute("advertencia", advertencia);
            model.addAttribute("user", user);
            return "agregar_dinero_bol";
        }

        if (user.getSaldo() < monto) {
            String advertencia = "El monto que desea mover supera el saldo que tiene";
            model.addAttribute("advertencia", advertencia);
            model.addAttribute("user", user);
            return "agregar_dinero_bol";
        } else {
            for (Bolsillos bolsillo : bolsillos2) {
                if (bolId == bolsillo.getId()) {
                    bolsillo = bolsilloService.encontrarBolsillo(bolsillo);
                    bolsillo.setSaldo(bolsillo.getSaldo() + monto);
                    bolsilloService.guardar(bolsillo);
                }
            }
            for (Bolsillo bolsillo : user.getBolsillos()) {
                if (bolId == bolsillo.getId()) {
                    // bolsillo.setSaldo(bolsillo.getSaldo() + monto);
                    // en este metodo actualizo tanto al usuario como al bolsillo (instancias)
                    user.agregar_dinero_bolsillo(bolsillo, monto);
                }
            }
            for (Usuarios usuario : usuarios2) {
                if (user.getId() == usuario.getId()) {
                    usuario = usuarioService.encontrarUsuario(usuario);
                    usuario.setSaldo(user.getSaldo());
                    usuarioService.actualizar(usuario);
                    // user.setSaldo(user.getSaldo() - monto);
                }
            }
            model.addAttribute("user", user);
            actualizar_datos();
            return "bolsillos";
        }
    }

    @GetMapping("/ir_simulacion")
    public String ir_simulacion(Model model) {
        return "simulacion";
    }

    @GetMapping("/ir_simulacion_gasto")
    public String ir_simulacion_gasto(Model model) {
        String advertencia2 = "";
        model.addAttribute("advertencia2", advertencia2);
        model.addAttribute("user", user);
        return "simulacion_gasto";
    }

    @GetMapping("/ir_simulacion_ingreso")
    public String ir_simulacion_ingreso(Model model) {
        String advertencia3 = "";
        model.addAttribute("advertencia3", advertencia3);
        return "simulacion_ingreso";
    }

    @PostMapping("/simulacion_gasto")
    public String simulacion_gasto(Model model, @RequestParam("metodo_pago") int opcion,
            @RequestParam("monto") double monto) {
        // validacion de que el monto sea positivo
        if ((monto) < 0) {
            String advertencia2 = "El monto no puede ser negativo";
            model.addAttribute("advertencia2", advertencia2);
            model.addAttribute("user", user);
            return "simulacion_gasto";
        }
        Transacciones tra = new Transacciones();
        // si se eligio pagar con el saldo principal
        if (opcion == -1) {
            if (user.getSaldo() < monto) {
                String advertencia2 = "El monto supera el saldo disponible";
                model.addAttribute("advertencia2", advertencia2);
                model.addAttribute("user", user);
                return "simulacion_gasto";
            } else {
                tra.setUsuario_id(user.getId());
                tra.setTipo("gasto");
                tra.setFecha(LocalDate.now().toString());
                tra.setMonto(monto);
                transaccionService.guardar(tra);
                user.simular_gasto(tra.getId(), monto);
                for (Usuarios usuario : usuarios2) {
                    if (usuario.getId() == user.getId()) {
                        usuario = usuarioService.encontrarUsuario(usuario);
                        usuario.setSaldo(user.getSaldo());
                        usuarioService.actualizar(usuario);
                    }
                }
            }
        } else {
            for (Bolsillo bol : user.getBolsillos()) {
                if (bol.getId() == opcion) {
                    if (bol.getSaldo() < monto) {
                        String advertencia2 = "El monto supera el saldo disponible";
                        model.addAttribute("advertencia2", advertencia2);
                        model.addAttribute("user", user);
                        return "simulacion_gasto";
                    }
                }
            }
            // si se eligio pagar con uno de los bolsillos
            tra.setUsuario_id(user.getId());
            tra.setTipo("gasto");
            tra.setFecha(LocalDate.now().toString());
            tra.setMonto(monto);
            transaccionService.guardar(tra);
            // se actualiza el bolsillo en la base da datos
            for (Bolsillos bol : bolsillos2) {
                if (bol.getId() == opcion) {
                    bol = bolsilloService.encontrarBolsillo(bol);
                    bol.setSaldo(bol.getSaldo() - monto);
                    bolsilloService.guardar(bol);
                }
            }
            // se actualiza la instancia del bolsillo
            for (Bolsillo bol : user.getBolsillos()) {
                if (bol.getId() == opcion) {
                    user.simular_gasto(tra.getId(), monto, bol.getId());
                }
            }
        }
        System.out.println("Transaccion del usuario: ");
        for (Transaccion tran : user.getTransacciones()) {
            System.out.println("#" + tran.getId() + ", " + tran.getUsuario_id() + ", " + tran.getTipo() + " - "
                    + tran.getMonto() + " - " + tran.getFecha());
        }
        fecha_actual = LocalDate.now().toString();
        model.addAttribute("fecha_actual", fecha_actual);
        model.addAttribute("user", user);
        actualizar_datos();
        return "menu";
    }

    @PostMapping("/simulacion_ingreso")
    public String simulacion_ingreso(Model model, @RequestParam("monto") double monto) {
        // validacion de que el monto sea positivo
        if ((monto) < 0) {
            String advertencia3 = "El monto no puede ser negativo";
            model.addAttribute("advertencia3", advertencia3);
            return "simulacion_ingreso";
        } else {
            Transacciones tra = new Transacciones();
            tra.setUsuario_id(user.getId());
            tra.setTipo("ingreso");
            tra.setFecha(LocalDate.now().toString());
            tra.setMonto(monto);
            transaccionService.guardar(tra);
            user.simular_ingreso(tra.getId(), monto);
            for (Usuarios usuario : usuarios2) {
                if (usuario.getId() == user.getId()) {
                    usuario = usuarioService.encontrarUsuario(usuario);
                    usuario.setSaldo(user.getSaldo());
                    usuarioService.actualizar(usuario);
                }
            }
            fecha_actual = LocalDate.now().toString();
            model.addAttribute("fecha_actual", fecha_actual);
            model.addAttribute("user", user);
            actualizar_datos();
            return "menu";
        }
    }

    @GetMapping("ir_transferencia")
    public String ir_transferencia(Model model) {
        String advertencia4 = "";
        model.addAttribute("advertencia4", advertencia4);
        model.addAttribute("user", user);
        return "transferencia";
    }

    @PostMapping("/transferencia")
    public String transferencia(Model model, @RequestParam("metodo_pago") int opcion,
            @RequestParam("monto") double monto, @RequestParam("id_usuario_destino") int id_usuario_destino) {
        boolean encontrado = false;
        for (Usuarios usuario : usuarios2) {
            if (usuario.getId() == id_usuario_destino) {
                encontrado = true;
            }
        }
        if (encontrado == false) {
            String advertencia4 = "El id de usuario que ingreso no se encuentra registrado";
            model.addAttribute("advertencia4", advertencia4);
            model.addAttribute("user", user);
            return "transferencia";
        }
        // validacion de que el monto sea positivo
        if ((monto) < 0) {
            String advertencia4 = "El monto no puede ser negativo";
            model.addAttribute("advertencia4", advertencia4);
            model.addAttribute("user", user);
            return "transferencia";
        }
        Transacciones tra = new Transacciones();
        // si se eligio pagar con el saldo principal
        if (opcion == -1) {
            if (user.getSaldo() < monto) {
                String advertencia4 = "El monto supera el saldo disponible";
                model.addAttribute("advertencia4", advertencia4);
                model.addAttribute("user", user);
                return "transferencia";
            } else {
                tra.setUsuario_id(user.getId());
                tra.setTipo("gasto");
                tra.setFecha(LocalDate.now().toString());
                tra.setMonto(monto);
                transaccionService.guardar(tra);
                user.simular_gasto(tra.getId(), monto);
                for (Usuarios usuario : usuarios2) {
                    if (usuario.getId() == user.getId()) {
                        usuario = usuarioService.encontrarUsuario(usuario);
                        usuario.setSaldo(user.getSaldo());
                        usuarioService.actualizar(usuario);
                    }
                }
            }
        } else {
            for (Bolsillo bol : user.getBolsillos()) {
                if (bol.getId() == opcion) {
                    if (bol.getSaldo() < monto) {
                        String advertencia4 = "El monto supera el saldo disponible";
                        model.addAttribute("advertencia4", advertencia4);
                        model.addAttribute("user", user);
                        return "transferencia";
                    }
                }
            }
            // si se eligio pagar con uno de los bolsillos
            tra.setUsuario_id(user.getId());
            tra.setTipo("gasto");
            tra.setFecha(LocalDate.now().toString());
            tra.setMonto(monto);
            transaccionService.guardar(tra);
            // se actualiza el bolsillo en la base da datos
            for (Bolsillos bol : bolsillos2) {
                if (bol.getId() == opcion) {
                    bol = bolsilloService.encontrarBolsillo(bol);
                    bol.setSaldo(bol.getSaldo() - monto);
                    bolsilloService.guardar(bol);
                }
            }
            // se actualiza la instancia del bolsillo
            for (Bolsillo bol : user.getBolsillos()) {
                if (bol.getId() == opcion) {
                    user.simular_gasto(tra.getId(), monto, bol.getId());
                }
            }
        }
        // el usuario al que se le transfiere recibe el dinero en su saldo principal, y
        // se le genera una transaccion de ingreso
        for (Usuarios usuario2 : usuarios2) {
            if (usuario2.getId() == id_usuario_destino) {
                // se le agrega el dinero que se le envio a su saldo principal y se actualiza la
                // base de datos
                usuario2 = usuarioService.encontrarUsuario(usuario2);
                usuario2.setSaldo(usuario2.getSaldo() + monto);
                usuarioService.actualizar(usuario2);
                // se le genera la transaccion de ingreso
                Transacciones tra2 = new Transacciones();
                tra2.setUsuario_id(usuario2.getId());
                tra2.setTipo("ingreso");
                tra2.setFecha(LocalDate.now().toString());
                tra2.setMonto(monto);
                transaccionService.guardar(tra2);
                // se le genera una notificacion al usuario destino de que le acaban de enviar
                // dinero
                Notificaciones noti = new Notificaciones();
                noti.setUsuario_id(usuario2.getId());
                noti.setMensaje("El usuario " + user.getUsuario() + " le acaba de transferir $" + monto);
                notificacionService.guardar(noti);
            }
        }
        fecha_actual = LocalDate.now().toString();
        model.addAttribute("fecha_actual", fecha_actual);
        model.addAttribute("user", user);
        actualizar_datos();
        return "menu";
    }

    @GetMapping("/eliminar_notificacion/{id}")
    public String eliminar_notificacion(Notificaciones notificacion) {
        for (Notificacion noti : user.getNotificaciones()) {
            if (noti.getId() == notificacion.getId()) {
                user.eliminar_notificacion(noti);
                break;
            }
        }
        notificacionService.eliminar(notificacion);
        notificaciones2.remove(notificacion);
        return "redirect:/ir_notificaciones";
    }

    @GetMapping("/ir_movimientos")
    public String ir_movimientos(Model model) {
        model.addAttribute("user", user);
        return "movimientos";
    }

    @GetMapping("/descargar")
    public void descargarTransacciones(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=transacciones.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Fecha, Tipo, Monto");
            for (Transaccion transaccion : user.getTransacciones()) {
                writer.println(transaccion.getFecha() + ", " + transaccion.getTipo() + ", " + transaccion.getMonto());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/ir_cambiar_usuario")
    public String ir_cambiar_usuario(Model model) {
        model.addAttribute("user", user);
        return "cambiar_usuario";
    }

    @PostMapping("/actualizar_cambio_usuario")
    public String cambiar_usuario(@RequestParam("usuario_nuevo") String usuario_nuevo) {
        for (Usuarios usuario : usuarios2) {
            if (usuario.getId() == user.getId()) {
                user.setUsuario(usuario_nuevo);
                usuario = usuarioService.encontrarUsuario(usuario);
                usuario.setUsuario(usuario_nuevo);
                usuarioService.actualizar(usuario);
            }
        }
        actualizar_datos();
        return "redirect:/regresar_menu";
    }

    @GetMapping("/ir_cambiar_correo")
    public String ir_cambiar_correo(Model model) {
        model.addAttribute("user", user);
        return "cambiar_correo";
    }

    @PostMapping("/actualizar_cambio_correo")
    public String cambiar_correo(@RequestParam("correo_nuevo") String correo_nuevo) {
        for (Usuarios usuario : usuarios2) {
            if (usuario.getId() == user.getId()) {
                user.setEmail(correo_nuevo);
                usuario = usuarioService.encontrarUsuario(usuario);
                usuario.setEmail(correo_nuevo);
                usuarioService.actualizar(usuario);
            }
        }
        return "redirect:/regresar_menu";
    }

    @GetMapping("/ir_notificaciones")
    public String ir_notificaciones(Model model) {
        model.addAttribute("user", user);
        return "notificaciones";
    }

    @PostMapping("/iniciarsesion")
    public String iniciarsesion(@RequestParam("input_usuario") String input_usuario,
            @RequestParam("input_contrasena") String input_contrasena, Model model) {
        for (Usuarios per : usuarios2) {
            if (per.getUsuario().equals(input_usuario)) {
                if (per.getContrasena().equals(input_contrasena)) {
                    equivocacion = false;
                    // se le añaden todos los datos de la base de datos a la instancia user segun el
                    // usuario que ingreso
                    user = new Usuario(per.getId(), per.getUsuario(), per.getContrasena(), per.getEmail(),
                            per.getFecha_ingreso(), per.getSaldo());
                    // se le añaden todos sus bolsillos
                    for (Bolsillos bol : bolsillos2) {
                        if (bol.getUsuario_id() == user.getId()) {
                            user.agregar_bolsillo(
                                    new Bolsillo(bol.getId(), bol.getUsuario_id(), bol.getNombre(), bol.getSaldo()));
                        }
                    }
                    // se le añaden todas sus transacciones
                    for (Transacciones tra : transacciones2) {
                        if (tra.getUsuario_id() == user.getId()) {
                            user.agregar_transaccion(new Transaccion(tra.getId(), user.getId(), tra.getTipo(),
                                    tra.getFecha(), tra.getMonto()));
                        }
                    }
                    // se le añaden todas sus notificaciones
                    for (Notificaciones noti : notificaciones2) {
                        if (noti.getUsuario_id() == user.getId()) {
                            user.agregar_notificacion(new Notificacion(noti.getId(), user.getId(), noti.getMensaje()));
                        }
                    }
                    model.addAttribute("user", user);
                    fecha_actual = LocalDate.now().toString();
                    model.addAttribute("fecha_actual", fecha_actual);
                    return "menu";
                }
            }
        }
        equivocacion = true;
        return "redirect:/login";
    }

    @GetMapping("/cambiar_contra")
    public String cambiar_contra(Model model) {
        String error = "";
        if (contramala2 == false) {
            error = "";
        } else {
            error = "contrasena ingresada incorrecta";
        }
        model.addAttribute("error", error);
        return "cambiar_contra";
    }

    @PostMapping("/cambiar_contrasena")
    public String cambiar_contrasena(@RequestParam("input_contra_actual") String input_contra_actual,
            @RequestParam("input_contra_nueva") String input_contra_nueva, Model model) {
        if (!input_contra_actual.equals(user.getContrasena())) {
            contramala2 = true;
            return "redirect:/cambiar_contra";
        } else {
            contramala2 = false;
            // actualizo la nueva contraseña en la base de datos
            for (Usuarios usuario : usuarios2) {
                if (usuario.getUsuario().equals(user.getUsuario())) {
                    if (user.getContrasena().equals(input_contra_actual)) {
                        usuario = usuarioService.encontrarUsuario(usuario);
                        usuario.setContrasena(input_contra_nueva);
                        usuarioService.actualizar(usuario);
                    }
                }
            }
            // actualizo la nueva contraseña en la instancia
            user.cambiar_contra(input_contra_actual, input_contra_nueva);
            model.addAttribute("user", user);
            fecha_actual = LocalDate.now().toString();
            model.addAttribute("fecha_actual", fecha_actual);
            actualizar_datos();
            return "menu";
        }
    }

}
