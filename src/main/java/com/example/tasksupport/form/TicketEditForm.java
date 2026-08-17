package com.example.tasksupport.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TicketEditForm {

    @NotBlank(message = "タイトルを入力してください。")
    @Size(min = 3, max = 50, message = "タイトルは3文字以上50文字以内で入力してください。")
    private String title;

    private String priority;

    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
