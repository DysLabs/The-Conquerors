<!--
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400"></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td></td>
		<td></td>
	</tr>
</table>
-->
# The Conquerors
The Conquerors is a fairly popular game on roblox.com. I decided to see if I could reimplement it in the Java programming language. This version will not be a carbon copy and eventually I will give it its one identity, that is it will not follow updates to the Roblox game.

# Protocol Documentation
This documentation will always be up-to-date. It is intended to run on a TCP connection, but the protocol format (I feel) would work well with UDP, however I find UDP to be much more difficult, and the fact that there is still a chance for packet dropping using UDP. Anyways, if you want to do your own thing with the game here's the protocol:
<table border="1" class="wikitable">
	<tr>
		<th>Name</th>
		<th>Size (Bytes)</th>
		<th>Encodes</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td>Boolean</td>
		<td>1</td>
		<td>true/false</td>
		<td></td>
	</tr>
	<tr>
		<td>Byte</td>
		<td>1</td>
		<td>An integer between -128 and 127</td>
		<td>Singed 8-bit integer, two's complement</td>
	</tr>
	<tr>
		<td>Short</td>
		<td>2</td>
		<td>An integer between -32768 and 32767</td>
		<td>Singed 16-bit integer, two's complement</td>
	</tr>
	<tr>
		<td>Int</td>
		<td>4</td>
		<td>An integer between -2147483648 and 2147483647</td>
		<td>Singed 32-bit integer, two's complement</td>
	</tr>
	<tr>
		<td>Long</td>
		<td>8</td>
		<td>An integer between -9223372036854775808 and 9223372036854775807</td>
		<td>Singed 64-bit integer, two's complement</td>
	</tr>
	<tr>
		<td>Float</td>
		<td>4</td>
		<td>A single-precision 32-bit IEEE 754 floating point number</td>
		<td></td>
	</tr>
	<tr>
		<td>Double</td>
		<td>8</td>
		<td>A double-precision 64-bit IEEE 754 floating point number</td>
		<td></td>
	</tr>
	<tr>
		<td>String</td>
		<td>Varies</td>
		<td>Text</td>
		<td>A series of shorts, prefixed by an integer of the size of the string</td>
	</tr>
</table>
<p>Note that while the official implementation is in Java, there is no 'char' data type. Any 'char's are automagically converted into shorts.</p>
</br>Serverbound means packets that are sent to the server from the client, while clientside is the inverse.<br/>Packets are formatted as:
<table border="1" class="wikitable">
	<tr><th>Field</th>
	<th>Field Type</th><th>Notes</th></tr>
	<tr>
		<td>Packet ID</td>
		<td>Int</td>
		<td></td>
	</tr>
	<tr>
		<td>Packet-Specific Data</td>
		<td>Varies</td>
		<td>See below</td>
		</tr>
</table>



<h2>Login</h2>
<p>When a client connects to the server</p>

<h3>Serverbound</h3>
<h4>Packet 0 Login</h4>
<table border="1" width="100%">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="2">0</td>
		<td>Name</td>
		<td>String</td>
		<td>The username supplied by the user</td>
	</tr>
	<tr>
		<td>Protocol Version</td>
		<td>Int</td>
		<td>The version of the protocol being used, currently '0'</td>
	</tr>
</table>

<h3>Clientbound</h3>

<h4>Packet 1 Login Success</h4>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">1</td>
		<td>Player List Length</td>
		<td>Byte</td>
		<td>The number of players currently online</td>
	</tr>
	<tr>
		<td>Player</td>
		<td>String</td>
		<td>The name of a player.<br/>This field continues for n where n is the value of the previous field.</td>
	</tr>
</table>

<h4>Packet 2 Login Failure</h4>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">2</td>
		<td>Reason</td>
		<td>String</td>
		<td>The reason for denial that will be displayed to the user</td>
	</tr>
</table>

<h2>Entity</h2>

<h3>Serverbound</h3>
<h4>Packet 4 Request Model</h4>
<p>If the client does not have a model</p>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">4</td>
		<td>Model</td>
		<td>String</td>
		<td></td>
	</tr>
</table>

<h4>Packet 10 Player Position</h4>
<p>Sent when the player's position has changed</p>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">10</td>
		<td>Translate X</td>
		<td>Float</td>
		<td rowspan="400">Movement in a given vector component</td>
	</tr>
	<tr>
		<td>Translate Y</td>
		<td>Float</td>
	</tr>
	<tr>
		<td>Translate Z</td>
		<td>Flaot</td>
	</tr>
</table>

<h4>Packet 11 Player Look</h4>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">11</td>
		<td>Rotate X</td>
		<td>Float</td>
		<td rowspan="400">Rotation in a given vector component</td>
	</tr>
	<tr>
		<td>Rotate Y</td>
		<td>Float</td>
	</tr>
	<tr>
		<td>Rotate Z</td>
		<td>Float</td>
	</tr>
</table>

<h3>Clientbound</h3>
<h4>Packet 3 Model</h4>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">3</td>
		<td>Model</td>
		<td>String</td>
		<td>Name of the model</td>
	</tr>
	<tr>
		<td>Model Length</td>
		<td>Int</td>
		<td>The number of times a Byte must be read to completely download the model</td>
	</tr>
	<tr>
		<td>Model</td>
		<td>Byte</td>
		<td>One Byte of the model</td>
	</tr>
</table>

<h4>Packet 5 Spawn Entity</h4>
<p>When an object suddenly comes into view</p>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">5</td>
		<td>Model</td>
		<td>String</td>
		<td>The game engine uses this to locate the model of an entity</td>
	</tr>
	<tr>
		<td>Material</td>
		<td>String</td>
		<td>The game engine uses this to display the entity</td>
	</tr>
	<tr>
		<td>Spatial ID</td>
		<td>String</td>
		<td>Game engine ID</td>
	</tr>
</table>

<h4>Packet 6 Check Model</h4>
<p>Sent to the client to confirm that the client has a model. If not, the client can request it with Packet 4 Request Model and the server will provide with a Packet 3 Model</p>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">6</td>
		<td>Model</td>
		<td>String</td>
		<td></td>
	</tr>
</table>

<h4>Packet 7 Translate Entity</h4>
<p>Sets the position of the entity relative to 0,0,0</p>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">7</td>
		<td>Spatial ID</td>
		<td>String</td><td>The ID of the entity as used by the game engine</td>
	</tr>
	<tr>
		<td>Translate X</td>
		<td>Float</td>
		<td rowspan="400">Movement in a given vector component</td>
	</tr>
	<tr>
		<td>Translate Y</td>
		<td>Float</td>
	</tr>
	<tr>
		<td>Translate Z</td>
		<td>Float</td>
	</tr>
</table>

<h4>Packet 8 Scale Entity</h4>
<p>Sets the size of the entity relative to its inital size</p>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">8</td>
		<td>Spatial ID</td>
		<td>String</td>
		<td>The ID of the entity as used by the game engine</td>
	</tr>
	<tr>
		<td>Scale X</td>
		<td>Float</td>
		<td rowspan="400">Scale in a given vector component</td>
	</tr>
	<tr>
		<td>Scale Y</td>
		<td>Float</td>
	</tr>
	<tr>
		<td>Scale Z</td>
		<td>Float</td>
	</tr>
</table>

<h4>Packet 9 Rotate Entity</h4>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">9</td>
		<td>Spatial ID</td>
		<td>String</td><td>The ID of the entity as used by the game engine</td>
	</tr>
	<tr>
		<td>Rotate X</td>
		<td>Float</td>
		<td rowspan="400">Rotation in a given vector component</td>
	</tr>
	<tr>
		<td>Rotate Y</td>
		<td>Float</td>
		<td></td>
	</tr>
	<tr>
		<td>Rotate Z</td>
		<td>Float</td>
		<td></td>
	</tr>
</table>

<h4>Packet 12 Remove Entity</h4>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">12</td>
		<td>Spatial ID</td>
		<td>String</td>
		<td></td>
	</tr>
</table>

<h2>Player</h2>
<h3>Serverbound</h3>
<h4>Packet 14 Request Window</h4>
<p>Opens the control window associated with the given spatial object</p>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">14</td>
		<td>Spatial ID</td>
		<td>String</td>
		<td></td>
	</tr>
</table>

<h4>Packet 16 Disconnect</h4>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">16</td>
		<td></td>
		<td></td>
		<td></td>
</table>

<h4>Packet 17 Chat</h4>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">17</td>
		<td>Ally</td>
		<td>Boolean</td>
		<td>True if this is in ally chat, false otherwise</td>
	</tr>
	<tr>
		<td>Message</td>
		<td>String</td>
		<td></td>
	</tr>
</table>

<h3>Clientbound</h3>
<h4>Packet 13 Player List</h4>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">13</td>
		<td>List Count</td>
		<td>Byte</td>
		<td></td>
	</tr>
	<tr>
		<td>Player Name (or alliance)</td>
		<td>String</td>
		<td>This field repeats for as many as 'list count'</td>
	</tr>
</table>

<h4>Packet 15 Open Window</h4>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">15</td>
		<td>Title</td>
		<td>String</td>
		<td></td>
	</tr>
	<tr>
		<td>Slots</td>
		<td>Byte</td>
		<td>The numbers of options on the window</td>
	</tr>
	<tr>
		<td>Slot n</td>
		<td>String</td>
		<td>One option, this field repeats for the number of slots</td>
	</tr>
</table>

<h4>Packet 18 Chat</h4>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">18</td>
		<td>Sender</td>
		<td>String</td>
		<td></td>
	</tr>
	<tr>
		<td>Ally</td>
		<td>Boolean</td>
		<td></td>
	</tr>
	<tr>
		<td>Message</td>
		<td>String</td>
		<td></td>
	</tr>
</table>



<h2>Test</h2>
<h4>Packet 1397966893 Test</h4>
<p>This packet is not bound to either the server nor client, nor will it ever be sent.</p>
<table border="1" class="wikitable">
	<tr>
		<th>Packet ID</th>
		<th>Field Name</th>
		<th>Field Type</th>
		<th>Notes</th>
	</tr>
	<tr>
		<td rowspan="400">1397966893</td>
		<td>String</td>
		<td>String</td>
		<td>Random payload</td>
	</tr>
</table>
