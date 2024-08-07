package org.example.graphic.scene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.controller.connect.to.graphic.ExceptionListener;
import org.controller.connect.to.graphic.ExceptionPublisher;

public class CurrentExceptionListener implements ExceptionListener {
    @Override
    public void onEvent(Exception exception) {
            Popup.showError(exception);
    }
    {
        ExceptionPublisher.addEventListener(this);
    }
}
