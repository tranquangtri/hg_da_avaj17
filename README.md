### Mục lục
* [Nội dung đồ án](#content)
* [Luật chơi](#rule)
  * [Mục tiêu](#content/goal)
  * [Table](#content/table)
  * [Cách chơi](#content/howtoplay)
  * [Cách tính điểm](#content/score)
* [Hướng dẫn biên dịch và chạy](#build)
 
<a name="content"></a>
### 1. Nội dung đồ án
  #### Game Heart (Microsoft)
  + Hỗ trợ tối đa 04 người chơi qua mạng LAN (Internet).
  + Có chức năng cho máy đóng vai trò người chơi.
  + Giao diện đồ họa
  #### Thời hạn nộp bài
  + 23:55 - 18/06/2018
  #### Nội dung nộp: MSSV1-MSSV2-MSSV3.zip (MSSV1 nộp)
	+ File jar thực thi
	+ File ant đóng gói chương trình
	+ Source code
	+ Báo cáo
    + Cách thực hiện chương trình (3trang)
    + Các chức năng đạt được và Hướng dẫn sử dụng.
    + Phân chia công việc trong nhóm
<a name="rule"></a>
### 2. Luật chơi
<a name="content/goal"></a>
#### Mục tiêu:
- Ghi điểm ít nhất có thể.
<a name="content/table"></a>
#### Table:
- Hearts được chơi với 52 quân bài. Đối thủ của bạn là 3 người. Mỗi người có 13 quân.
<a name="content/howtoplay"></a>
#### Cách chơi: (hand: lượt chơi, trick: nước đi, suit: loại quân)
- Người chơi bắt đầu mỗi lượt chơi bằng cách chuyển 3 quân bài cho đối thủ của họ (lượt chơi thứ 1: chuyển cho ng bên trái, thứ 2: chuyển cho ng bên phải, thứ 3: chuyển cho ng đối diện, thứ 4: ko chuyển).
- Sau đó, người giữ quân 2 chuồn sẽ đi nước đầu tiên.
- Tiếp theo, theo chiều kim đồng hồ, lần lượt từng người chơi đi 1 quân, quân này phải cùng loại với quân của người mở đầu nước đi (trừ trường hợp ko có quân nào loại đó thì có thể đi bất kì quân nào (trừ nước đi đầu tiên, ta không thể đi quân đầm pích hay quân cơ – để đảm bảo luật “không ghi điểm ở nước đi đầu tiên&rdquo.
- Ai đi quân lớn nhất (và cùng loại với người mở đầu nước đi) sẽ thắng nước đi đó và bắt đầu nước đi kế tiếp. Trong hearts, quân bài được xếp hạng từ ace (cao nhất) đến 2(thấp nhất).
- Người chơi có thể bắt đầu nước đi tiếp theo với bất kì loại quân bài nào, trừ quân Cơ. Bạn k thể đi quân Cơ trừ phi đã có người đi quân Cơ trong nước đi trước đó. (Hay theo cách nói trong game là cho tới khi Hearts have been broken – trái tim bị vỡ).
- Mục đích trong game là chuyển hết tất cả quân Cơ cho những người chơi khác (những người cũng đang cố gắng chuyển chúng cho bạn). Game kết thúc khi 1 người chơi đạt 100 điểm. Khi đó, người chơi có số điểm thấp nhất là người thắng cuộc.
<a name="content/score"></a>
#### Cách tính điểm:
- Mỗi quân Cơ trong một nước đi được tính 1 điểm.
- Quân Đầm Pích được tính 13 điểm.
(nguồn: http://vietgamedev.net/forum/thread/235/clone-game-hearts-game-%C4%91%C3%A1nh-b%C3%A0i-tr%C3%AAn-windows/)

### 3. Hướng dẫn biên dịch và chạy
- Cài JDK 1.8: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
- Cài Gradle (3.5): https://gradle.org/releases
- Để biên dịch"
```
gradle build
```


