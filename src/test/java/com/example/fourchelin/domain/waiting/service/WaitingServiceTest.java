package com.example.fourchelin.domain.waiting.service;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.enums.MemberRole;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.exception.StoreException;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import com.example.fourchelin.domain.waiting.dto.request.WaitingRequest;
import com.example.fourchelin.domain.waiting.dto.response.WaitingResponse;
import com.example.fourchelin.domain.waiting.entity.Waiting;
import com.example.fourchelin.domain.waiting.enums.WaitingMealType;
import com.example.fourchelin.domain.waiting.enums.WaitingStatus;
import com.example.fourchelin.domain.waiting.enums.WaitingType;
import com.example.fourchelin.domain.waiting.exception.WaitingAlreadyExistException;
import com.example.fourchelin.domain.waiting.exception.WaitingNotAuthorizedException;
import com.example.fourchelin.domain.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.fourchelin.domain.member.entity.Member.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingServiceTest {
    @Mock
    private WaitingRepository waitingRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private WaitingService waitingService;

    @Nested
    @DisplayName("웨이팅 신청")
    class waitingService_createWaiting {
        @Test
        @DisplayName("웨이팅 신청 성공")
        void createWaiting_Success() {
            // given
            WaitingRequest request = new WaitingRequest(WaitingType.MOBILE, WaitingMealType.DINE_IN, 2, 1L);
            Member member = createMember("01012345678", "nickname", "password", MemberRole.USER);
            Store store = new Store(1L, "Test Store", "서울시 강남구");

            when(waitingRepository.existsByMemberIdAndStoreIdAndStatus(member.getId(), store.getId(), WaitingStatus.WAITING)).thenReturn(false);
            when(storeRepository.findById(request.storeId())).thenReturn(Optional.of(store));

            // when
            WaitingResponse response = waitingService.create(request, member);

            // then
            assertThat(response).isNotNull();
            assertThat(response.storeName()).isEqualTo("Test Store");
            assertThat(response.waitingStatus()).isEqualTo(WaitingStatus.WAITING);
            verify(waitingRepository, times(1)).save(any(Waiting.class));
        }

        @Test
        @DisplayName("이미 대기중인 경우 예외 발생")
        void createWaiting_AlreadyWaiting_Exception() {
            // given
            WaitingRequest request = new WaitingRequest(WaitingType.MOBILE, WaitingMealType.DINE_IN, 2, 1L);
            Member member = createMember("01012345678", "nickname", "password", MemberRole.USER);

            when(waitingRepository.existsByMemberIdAndStoreIdAndStatus(member.getId(), 1L, WaitingStatus.WAITING)).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> waitingService.create(request, member))
                    .isInstanceOf(WaitingAlreadyExistException.class)
                    .hasMessage("이미 예약된 사항이 존재합니다.");
        }

        @Test
        @DisplayName("가게가 없는 경우 예외 발생")
        void createWaiting_StoreNotFound_Exception() {
            // given
            WaitingRequest request = new WaitingRequest(WaitingType.MOBILE, WaitingMealType.DINE_IN, 2, 1L);
            Member member = createMember("01012345678", "nickname", "password", MemberRole.USER);

            when(waitingRepository.existsByMemberIdAndStoreIdAndStatus(member.getId(), 1L, WaitingStatus.WAITING)).thenReturn(false);
            when(storeRepository.findById(request.storeId())).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> waitingService.create(request, member))
                    .isInstanceOf(StoreException.class)
                    .hasMessage("해당 가게가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("웨이팅 취소")
    class waitingService_delete{
        @Test
        @DisplayName("웨이팅 취소 성공")
        void delete_Success() {
            // given
            Member member = mock(Member.class);
            when(member.getId()).thenReturn(1L);

            Waiting waiting = Waiting.builder()
                    .member(member)
                    .store(new Store(1L, "Test Store", "서울시 강남구"))
                    .type(WaitingType.MOBILE)
                    .mealType(WaitingMealType.DINE_IN)
                    .status(WaitingStatus.WAITING)
                    .waitingNumber(1)
                    .personnel(2)
                    .build();

            when(waitingRepository.findById(1L))
                    .thenReturn(Optional.of(waiting));

            // when
            waitingService.delete(1L, member);

            //then
            verify(waitingRepository, times(1)).delete(waiting);
        }

        @Test
        @DisplayName("웨이팅 취소 권한이 없을 경우 웨이팅 실패")
        void delete_FailByAuthorizationException() {
            // given
            Member member = mock(Member.class);
            when(member.getId()).thenReturn(2L);

            Member member2 = mock(Member.class);
            when(member.getId()).thenReturn(1L);

            Waiting waiting = Waiting.builder()
                    .member(member2)
                    .store(new Store(1L, "Test Store", "서울시 강남구"))
                    .type(WaitingType.MOBILE)
                    .mealType(WaitingMealType.DINE_IN)
                    .status(WaitingStatus.WAITING)
                    .waitingNumber(1)
                    .personnel(2)
                    .build();
            when(waitingRepository.findById(1L))
                    .thenReturn(Optional.of(waiting));

            // when & then
            assertThatThrownBy(() -> waitingService.delete(1L, member))
                    .isInstanceOf(WaitingNotAuthorizedException.class)
                    .hasMessage("해당 웨이팅을 취소할 권한이 없습니다.");
        }
    }
}
