package com.marvel.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table
@Entity(name = "creators")
public class Creator implements Serializable {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @Column(name = "creator_code")
	private Long creatorCode;
	@Column(name = "first_name")
	private String firtsName;
	@Column(name = "middle_name")
	private String middleName;
	@Column(name = "last_name")
	private String lastName;
	private static final long serialVersionUID = -829043409766557409L;

}
