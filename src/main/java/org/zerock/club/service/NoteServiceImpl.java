package org.zerock.club.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.club.dto.NoteDTO;
import org.zerock.club.entity.Note;
import org.zerock.club.repository.NoteRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    // 등록
    @Override
    public Long register(NoteDTO noteDTO) {

        Note note = dtoToEntity(noteDTO);
        log.info("===================");
        log.info(note);

        noteRepository.save(note);

        return note.getNum();
    }

    // 작성자 정보
    @Override
    public NoteDTO get(Long num) {

        Optional<Note> result = noteRepository.getWithWriter(num);

        if(result.isPresent()) { return entityToDTO(result.get()); }

        return null;
    }

    // 수정
    @Override
    public void modify(NoteDTO noteDTO) {

        Long num = noteDTO.getNum();

        // 수정할 글을 먼저 null이 아닌지 확인.
        Optional<Note> result = noteRepository.getWithWriter(num);

        // 수정할 글이 있다면 수정할 내용만 변경하고 저장.
        if (result.isPresent()) {
            Note note = result.get();

            note.changeTitle(noteDTO.getTitle());
            note.changeContent(noteDTO.getContent());

            noteRepository.save(note);
        }
    }

    // 삭제
    @Override
    public void remove(Long num) {
        noteRepository.deleteById(num);
    }

    // 작성자 전체 글
    @Override
    public List<NoteDTO> getAllWithWriter(String writerEmail) {

        List<Note> noteList = noteRepository.getList(writerEmail);

        return noteList.stream().map(note -> entityToDTO(note)).collect(Collectors.toList());

/*        return (List)noteList.stream().map(new Function<Note, Object>() {
            @Override
            public NoteDTO apply(Note note) {
                return entityToDTO(note);
            }
        });*/
    }

}
