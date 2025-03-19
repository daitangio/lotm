package com.gioorgi.lotm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class LotmVersionHistory {
    
    @Id
    @SequenceGenerator(name="s_version_history")
    @Column
    Long id;

    @Column
    String comment;
}
