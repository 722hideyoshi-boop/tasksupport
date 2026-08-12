package com.example.tasksupport.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TicketRegisterForm {

    @NotBlank(message = "タイトルを入力してください。")
    @Size(min = 3, max = 50, message = "タイトルは3文字以上50文字以内で入力してください。")
    private String title;

    private String priority;

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getPriority() {

        return priority;
    }

    public void setPriority(String priority) {

        this.priority = priority;
    }

}
