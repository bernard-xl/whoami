package xl.application.hr.whoami.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import xl.application.hr.whoami.util.UniqueIdentifierGenerator;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleResumeDaoTest {

    private ResumeDao dao;

    @Before
    public void setup() {
        AtomicInteger seq = new AtomicInteger();
        UniqueIdentifierGenerator ids = mock(UniqueIdentifierGenerator.class);
        when(ids.next()).then(new AnswerStringSequences());
        dao = new SimpleResumeDao(ids);
    }

    @Test
    public void resumeIsPersisted() {
        Resume record = dao.persist(new Resume(null, "candidate", "tester", "company", "something"));
        assertEquals("candidate", record.getName());
        assertEquals("tester", record.getTitle());
        assertEquals("company", record.getCompany());
        assertEquals("something", record.getDescription());
    }

    @Test
    public void resumeCouldBeFoundById() {
        Resume record = dao.persist(new Resume(null, "candidate", "tester", "company", "something"));
        Optional<Resume> found = dao.findById(record.getId());
        assertEquals(Optional.of(record), found);
    }

    @Test
    public void resumeCouldBeSearch() {
        Resume tomResume = dao.persist(new Resume(null, "Tom", "Sales", "Airplane Company", "I'm selling everything that files"));
        Resume jerryResume = dao.persist(new Resume(null, "Jerry", "Developer", "Geeky Company", "Trust me, I'm engineer"));
        Resume sinaResume = dao.persist(new Resume(null, "Sina", "Consultant", "Answer Company", "I'm problem solver"));

        List<Resume> foundById = dao.searchByAllFields(tomResume.getId());
        assertTrue(foundById.contains(tomResume));

        List<Resume> foundByName = dao.searchByAllFields(jerryResume.getName());
        assertTrue(foundByName.contains(jerryResume));

        List<Resume> foundByCompany = dao.searchByAllFields(sinaResume.getCompany());
        assertTrue(foundByCompany.contains(sinaResume));

        List<Resume> foundByKeyword = dao.searchByAllFields("engineer");
        assertTrue(foundByKeyword.contains(jerryResume));
    }

    private static class AnswerStringSequences implements Answer<String> {

        private int i = 0;

        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
            return String.valueOf(i++);
        }
    }
}
