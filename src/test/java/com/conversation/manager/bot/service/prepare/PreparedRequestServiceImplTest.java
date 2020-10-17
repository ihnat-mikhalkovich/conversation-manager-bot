package com.conversation.manager.bot.service.prepare;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.service.request.TelegramRequestService;
import com.conversation.manager.bot.telegram.method.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.function.SupplierUtils;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PreparedRequestServiceImplTest {

    @Autowired
    private PreparedRequestService preparedRequestService;

    @MockBean
    private TelegramRequestService telegramRequestService;

    @BeforeEach
    public void setUpCheckChatMemberService() {
        final SaveGetChatMember saveGetChatMember1 = new SaveGetChatMember();
        saveGetChatMember1.setUserId(101);
        saveGetChatMember1.setChatId(201L);
        when(telegramRequestService.existChatMember(saveGetChatMember1)).thenReturn(true);

        final SaveGetChatMember saveGetChatMember2 = new SaveGetChatMember();
        saveGetChatMember2.setUserId(101);
        saveGetChatMember2.setChatId(202L);
        when(telegramRequestService.existChatMember(saveGetChatMember2)).thenReturn(false);

        final SaveGetChatMember saveGetChatMember3 = new SaveGetChatMember();
        saveGetChatMember3.setUserId(101);
        saveGetChatMember3.setChatId(-1001246979812L);
        when(telegramRequestService.existChatMember(saveGetChatMember3)).thenReturn(false);

        final SaveGetChatMember saveGetChatMember4 = new SaveGetChatMember();
        saveGetChatMember4.setUserId(102);
        saveGetChatMember4.setChatId(201L);
        when(telegramRequestService.existChatMember(saveGetChatMember4)).thenReturn(true);

        final SaveGetChatMember saveGetChatMember5 = new SaveGetChatMember();
        saveGetChatMember5.setUserId(102);
        saveGetChatMember5.setChatId(202L);
        when(telegramRequestService.existChatMember(saveGetChatMember5)).thenReturn(true);

        final SaveGetChatMember saveGetChatMember6 = new SaveGetChatMember();
        saveGetChatMember6.setUserId(102);
        saveGetChatMember6.setChatId(-1001246979812L);
        when(telegramRequestService.existChatMember(saveGetChatMember6)).thenReturn(false);
    }

    @Test
    public void findOneGroup() {
        final int userId = 101;
        final Set<Group> expectedGroups = new HashSet<>();
        expectedGroups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(2L);
            group.setGroupId(201L);
            return group;
        }));

        final Set<Group> resultGroups = preparedRequestService.findGroupsByUserId(userId);

        assertEquals(expectedGroups, resultGroups);
    }

    @Test
    public void findTwoGroups() {
        final int userId = 102;
        final Set<Group> expectedGroups = new HashSet<>();
        expectedGroups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(2L);
            group.setGroupId(201L);
            return group;
        }));
        expectedGroups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(3L);
            group.setGroupId(202L);
            return group;
        }));

        final Set<Group> resultGroups = preparedRequestService.findGroupsByUserId(userId);

        assertEquals(expectedGroups, resultGroups);
    }

    @Test
    public void testFindChatsWithLinks() {
        final TelegramRequestService mockSend = mock(TelegramRequestService.class);

        final GetChat getChat1 = new GetChatWithEquals();
        getChat1.setChatId(201L);
        final Chat chat1 = mock(ChatWithEquals.class);
        when(chat1.getId()).thenReturn(201L);
        when(chat1.getInviteLink()).thenReturn("link1");
        when(mockSend.getChat(getChat1)).thenReturn(Optional.of(chat1));

        final GetChat getChat2 = new GetChatWithEquals();
        getChat2.setChatId(202L);
        final Chat chat2 = mock(ChatWithEquals.class);
        when(chat2.getId()).thenReturn(202L);
        when(chat2.getInviteLink()).thenReturn("link2");
        when(mockSend.getChat(getChat2)).thenReturn(Optional.of(chat2));

        final GetChat getChat3 = new GetChatWithEquals();
        getChat3.setChatId(203L);
        when(mockSend.getChat(getChat3)).thenReturn(Optional.empty());

        final GetChat getChat4 = new GetChatWithEquals();
        getChat4.setChatId(204L);
        final Chat chat4 = mock(ChatWithEquals.class);
        when(chat4.getId()).thenReturn(204L);
        when(chat4.getInviteLink()).thenReturn("");
        when(mockSend.getChat(getChat4)).thenReturn(Optional.of(chat4));

        final ExportChatInviteLinkWithEquals exportChatInviteLinkWithEquals = new ExportChatInviteLinkWithEquals();
        exportChatInviteLinkWithEquals.setChatId(204L);
//        when(mockSend.exposeLink(exportChatInviteLinkWithEquals)).thenReturn("link4");

        final GetChat getChat4Updated = new GetChatWithEquals();
        getChat4Updated.setChatId(204L);
        final Chat chat4Updated = mock(ChatWithEquals.class);
        when(chat4Updated.getId()).thenReturn(204L);
        when(chat4Updated.getInviteLink()).thenReturn("link4");

        when(mockSend.exposeLink(exportChatInviteLinkWithEquals)).thenAnswer(invocation -> {
            final TelegramRequestService mock = (TelegramRequestService) invocation.getMock();
            when(mock.getChat(getChat4)).thenReturn(Optional.of(chat4Updated));
            return "link4";
        });

        final Set<Group> groups = new HashSet<>();
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(201L);
            return group;
        }));
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(202L);
            return group;
        }));
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(203L);
            return group;
        }));
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(204L);
            return group;
        }));

        final PreparedRequestService preparedRequestService = new PreparedRequestServiceImpl(mockSend, null);
        final Set<Chat> chats = preparedRequestService.findChats(groups);

        final Set<String> links = chats.stream().map(Chat::getInviteLink).collect(Collectors.toSet());
        assertThat(links).contains("link1");
        assertThat(links).contains("link2");
        assertThat(links).contains("link4");

        final Set<Long> idSet = chats.stream().map(Chat::getId).collect(Collectors.toSet());
        assertThat(idSet).contains(201L);
        assertThat(idSet).contains(202L);
        assertThat(idSet).doesNotContain(203L);
        assertThat(idSet).contains(204L);
    }

    @Test
    public void kickFromChatPositiveCase() {
        Long chatId = 100L;
        Integer userId = 200;
        final KickChatMember kickChatMember = new KickChatMemberWithEquals();
        kickChatMember.setUserId(userId);
        kickChatMember.setChatId(chatId);
        when(telegramRequestService.kickFromChat(kickChatMember)).thenReturn(true);

        final boolean result = preparedRequestService.kickFromChat(chatId, userId);

        assertThat(result).isTrue();
    }

    @Test
    public void kickFromChatNegativeCase() {
        Long chatId = 100L;
        Integer userId = 200;
        final KickChatMember kickChatMember = new KickChatMemberWithEquals();
        kickChatMember.setUserId(userId);
        kickChatMember.setChatId(chatId);
        when(telegramRequestService.kickFromChat(kickChatMember)).thenReturn(false);

        final boolean result = preparedRequestService.kickFromChat(chatId, userId);

        assertThat(result).isFalse();
    }
}