package ru.yandex.praktikum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Courier {
    private String login;
    private String password;
    private String firstName;
}
