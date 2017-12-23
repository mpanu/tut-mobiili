<CsoundSynthesizer>
<CsOptions>
-o dac -+rtmidi=null -d -+msg_color=0 -M0 -m0
</CsOptions>
<CsInstruments>
nchnls=2
0dbfs=1
ksmps=32
sr = 48000

instr 1

kaccelX chnget "accelerometerX" 
kaccelY chnget "accelerometerY" 
kaccelZ chnget "accelerometerZ" 

;kgyroX chnget "gyroX" 
;kgyroY chnget "gyroY" 
;kgyroZ chnget "gyroZ" 

;kattRoll chnget "attitudeRoll" 
;kattPitch chnget "attitudePitch" 
;kattYaw chnget "attitudeYaw" 
kaccelX port kaccelX / 20, 0.01
kaccelY port kaccelY / 20, 0.01
kaccelZ port kaccelZ / 20, 0.01

;kcutoff = 4000 + (2000 * kaccelX)
;kresonance = .6 + (.3  * kaccelY)
kpch = 500 + (kaccelX + kaccelY) * 1000

chnset kpch, "pitch"

a1 vco2 (.7 * .2) + .4, kpch

;a1 moogladder a1, kcutoff, kresonance

aL, aR reverbsc a1, a1, .72, 5000

out aL, aR


endin

</CsInstruments>
<CsScore>
f1 0 16384 10 1

i1 0 360000
 
</CsScore>
</CsoundSynthesizer>
<bsbPanel>
 <label>Widgets</label>
 <objectName/>
 <x>100</x>
 <y>100</y>
 <width>320</width>
 <height>240</height>
 <visible>true</visible>
 <uuid/>
 <bgcolor mode="nobackground">
  <r>255</r>
  <g>255</g>
  <b>255</b>
 </bgcolor>
</bsbPanel>
<bsbPresets>
</bsbPresets>
