package cl.veterinary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "INVENTORY")
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_seq")
    @SequenceGenerator(name = "pet_seq", sequenceName = "PET_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "STOCK")
    private Integer stock;

    @Column(name = "UNIDAD_MEDIDA")
    private String unidadMedida;

    @Column(name = "FECHA_VENCIMIENTO")
    private String fechaVencimiento;

}
