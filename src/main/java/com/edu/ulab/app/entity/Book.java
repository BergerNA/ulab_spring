package com.edu.ulab.app.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "book", schema = "ulab_edu")
public class Book extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(name = "person_id",
            nullable = false)
    @ToString.Exclude
    private User user;

    @Column(name = "title",
            nullable = false)
    private String title;

    @Column(name = "author",
            nullable = false)
    private String author;

    @Column(name = "page_count",
            nullable = false)
    private int pageCount;
}
