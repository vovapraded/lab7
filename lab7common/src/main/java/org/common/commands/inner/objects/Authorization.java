package org.common.commands.inner.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
@Getter @Setter
@NoArgsConstructor
public class Authorization implements Serializable {
    @Serial
    private static final long serialVersionUID = "Authorization".hashCode();
public Authorization(String login,String password){
    this.login = login;
    this.password = password;
}
    private String password;
    private String login;
}
