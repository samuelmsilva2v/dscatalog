package com.devsuperior.dscatalog.dtos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.devsuperior.dscatalog.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@Size(min = 3, max = 60, message = "O nome deve ter entre 3 e 60 caracteres")
	@NotBlank(message = "Campo obrigatório")
	private String firstName;
	
	@Size(max = 60, message = "O sobrenome deve ter no máximo 60 caracteres")
	@NotBlank(message = "Campo obrigatório")
	private String lastName;
	
	@Email(message = "Por favor, insira um e-mail válido")
	@NotBlank(message = "Campo obrigatório")
	private String email;
	
	Set<RoleDto> roles = new HashSet<>();
	
	public UserDto() {
		// TODO Auto-generated constructor stub
	}

	public UserDto(Long id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public UserDto(User entity) {
		id = entity.getId();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		email = entity.getEmail();
		entity.getRoles().forEach(role -> this.roles.add(new RoleDto(role)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<RoleDto> getRoles() {
		return roles;
	}
}
