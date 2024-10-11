package com.danny.customerms.model;

import com.danny.customerms.exception.BadPetitionException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String nombre;
  private String apellido;
  private String dni;
  private String email;

  public void setNombre(String nombre) {
    if (nombre == null || nombre.isEmpty() || nombre.isBlank()) {
      throw new BadPetitionException("Proporcione un nombre");
    }
    this.nombre = nombre;
  }

  public void setApellido(String apellido) {
    if (apellido == null || apellido.isEmpty() || apellido.isBlank()) {
      throw new BadPetitionException("Proporcione un apellido");
    }
    this.apellido = apellido;
  }

  public void setDni(String dni) {
    if (dni == null || dni.isBlank() || dni.length() != 8) {
      throw new BadPetitionException("DNI no valido, tiene que ser de 8 caracteres");
    }
    this.dni = dni;
  }

  public void setEmail(String email) {
    if (email == null || email.isEmpty() || email.isBlank()) {
      throw new BadPetitionException("Proporcione un email");
    }
    this.validateEmail(email);
    this.email = email;
  }

  private void validateEmail(String email) {
    String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(email);
    if (!matcher.matches()) {
      throw new BadPetitionException("Email invalido");
    }
  }
}
