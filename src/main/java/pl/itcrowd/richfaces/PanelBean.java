package pl.itcrowd.richfaces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.Random;

@ManagedBean
@RequestScoped
public class PanelBean implements Serializable {

    private Random generator = new Random();

    private String inputText;

    private String text;

    public String getInputText()
    {
        return inputText;
    }

    public void setInputText(String inputText)
    {
        this.inputText = inputText;
    }

    public int getRandomInterval()
    {
        return generator.nextInt(1000) + 100;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void changeText() throws InterruptedException
    {
        String a = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String b = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
        if (text.equals(a)) {
            text = b;
        } else {
            text = a;
        }
        int ms = generator.nextInt(600) + 1;
        Thread.sleep(ms);
    }

    public void longAction()
    {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException ignore) {
        }
    }
}
