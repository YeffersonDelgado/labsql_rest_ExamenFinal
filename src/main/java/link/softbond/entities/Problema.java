package link.softbond.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



import lombok.Data;


@Data
@Entity
public class Problema {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; 
	
	private String nombre; 
	private String descripcion; 
	private String docente; 
	private int estado; 
	private String nombreBase;

}
