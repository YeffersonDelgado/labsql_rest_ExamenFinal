package link.softbond.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import link.softbond.dto.UsuarioDTO;
import link.softbond.entities.Usuario;
import link.softbond.repositorios.UsuarioRepository;
//import com.encrypta.backendapp.model.entities.Rol;

@Service ("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
  
	    @Autowired
		private BCryptPasswordEncoder passwordEncoder;
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
	
	public Usuario registerNewUser(UsuarioDTO usuarioDto) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDto.getId());
        usuario.setUsuario(usuarioDto.getUsuario());
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setEstado("I"); // Set user status to Inactive
        usuario.setClave(passwordEncoder.encode(usuarioDto.getClave())); // Don't forget to encode the password

        return usuarioRepository.save(usuario); // Save the user
    }



	
	@Override
	@Transactional (readOnly = true)
	public UserDetails loadUserByUsername(String usuarioEmail) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(usuarioEmail);

		if (usuario == null) {
			logger.error("Error Login: no existe el usuario " + usuarioEmail);
			throw new UsernameNotFoundException("Usuario no existe en el sistema");
		}
		
		if (!usuario.getEstado().contentEquals("A")) {
			logger.error("Error Login: el usuario " + usuarioEmail + " aun no se ha validado");
			throw new UsernameNotFoundException("Usuario no se ha validado");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
		
		authorities.add(new SimpleGrantedAuthority("USUARIO"));

		return new User(usuario.getEmail(), usuario.getClave(), true, true, true, true, authorities);
	}

}
