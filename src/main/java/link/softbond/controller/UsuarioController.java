package link.softbond.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import link.softbond.auth.service.JWTService;
import link.softbond.dto.UsuarioDTO;
import link.softbond.entities.Usuario;
import link.softbond.repositorios.UsuarioRepository;
import link.softbond.service.JpaUserDetailsService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private JpaUserDetailsService usuarioService;
    
    private AuthenticationManager authenticationManager;
	private JWTService jwtService;

    @Autowired
	private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Usuario> registerNewUser(@RequestBody UsuarioDTO usuarioDto) {
        try {
            Usuario newUser = usuarioService.registerNewUser(usuarioDto);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/auth")
    public ResponseEntity<String> authenticateUser(@RequestBody UsuarioDTO userDto) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userDto.getUsuario(),
                userDto.getClave()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.create(authentication);
        return ResponseEntity.ok(jwt);
    }
}
