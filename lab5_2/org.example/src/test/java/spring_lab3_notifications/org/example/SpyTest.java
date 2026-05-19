package spring_lab3_notifications.org.example;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SpyTest {

    @Test
    void shouldSpyOnArrayList() {
        List<String> list = new ArrayList<>();
        List<String> spyList = spy(list);

        spyList.add("Spring");

        verify(spyList, times(1)).add("Spring");
        assertEquals(1, spyList.size());
        assertEquals("Spring", spyList.get(0));
    }
}
