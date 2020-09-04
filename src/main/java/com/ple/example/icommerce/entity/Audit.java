package com.ple.example.icommerce.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "audit")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Audit extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key")
    private Long key;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "action_type")
    @NotNull
    private AuditActionType actionType;

    @Column(name = "input_description")
    @Size(max = 1)
    private String inputDescription;

}
