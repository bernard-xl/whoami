package xl.application.hr.whoami.model;

import org.springframework.stereotype.Component;
import xl.application.hr.whoami.util.UniqueIdentifierGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
class SimpleResumeDao implements ResumeDao {

    private final UniqueIdentifierGenerator ids;
    private final ConcurrentHashMap<String, Resume> resumes;

    public SimpleResumeDao(UniqueIdentifierGenerator identifierGenerator) {
        ids = identifierGenerator;
        resumes = new ConcurrentHashMap<>();
    }

    @Override
    public Resume persist(Resume resume) {
        String id = ids.next();
        resume.setId(id);
        resumes.put(id, resume);
        return resume;
    }

    @Override
    public Optional<Resume> findById(String id) {
        return Optional.ofNullable(resumes.get(id));
    }

    @Override
    public List<Resume> searchByAllFields(String query) {
        ArrayList<Resume> foundById = new ArrayList<>();
        ArrayList<Resume> foundByName = new ArrayList<>();
        ArrayList<Resume> foundByTitle = new ArrayList<>();
        ArrayList<Resume> foundByCompany = new ArrayList<>();
        ArrayList<Resume> foundByDesc = new ArrayList<>();

        for (Resume resume : resumes.values()) {
            if (resume.getId().equals(query)) {
                foundById.add(resume);
            } else if (resume.getName().equals(query)) {
                foundByName.add(resume);
            } else if (resume.getTitle().equals(query)) {
                foundByTitle.add(resume);
            } else if (resume.getCompany().equals(query)) {
                foundByCompany.add(resume);
            } else {
                String[] words = resume.getDescription().split(" ");
                for (String word : words) {
                    if (word.equals(query)) {
                        foundByDesc.add(resume);
                        break;
                    }
                }
            }
        }

        ArrayList<Resume> result = new ArrayList<>(foundById);
        result.addAll(foundByName);
        result.addAll(foundByTitle);
        result.addAll(foundByCompany);
        result.addAll(foundByDesc);
        return result;
    }
}
