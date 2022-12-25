package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public Result<List<Member>> memberList(){
        List<MemberDto> collect = memberService.findMembers().stream()
                .map(m -> new MemberDto(m.getUsername()))
                .collect(Collectors.toList());
        return new Result(collect);
    }
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setUsername(request.name);
        member.setAddress(new Address(request.street, request.city, request.zipcode));
        Long id =memberService.join(member);
        return new CreateMemberResponse(id);
    }
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMember(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMemberRequest request){
        Long updatedId =memberService.update(id, request.name);
        Member member = memberService.findOne(updatedId);
        return new UpdateMemberResponse(member.getId(), member.getUsername() );
    }


    @RequiredArgsConstructor
    static class Result<T>{
        private final T data;
    }
    public record MemberDto(String name){}
    public record UpdateMemberResponse(Long id, String name){}
    public record UpdateMemberRequest(@NotEmpty String name){}
    public record CreateMemberRequest(@NotEmpty String name, String city, String zipcode, String street){}

    public record CreateMemberResponse(@NotNull Long id){}
}
