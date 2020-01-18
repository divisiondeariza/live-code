
use_bpm 100

live_loop :kick do
  sample :bd_haus
  sleep 1
end

live_loop :snare do
  sleep 0.75
  sample :drum_tom_mid_soft
  sleep 0.75
  with_fx :echo do |r|
    if one_in(12)
      control r, phase: 0.25
    else
      control r, mix:0
    end
    sample :sn_dub
  end
  sleep 0.5
end

live_loop :hats do
  sample :drum_cymbal_closed
  sleep 0.5
end

live_loop :breakfast do
  sync :kick
  sample :loop_breakbeat, beat_stretch: 4, amp:2, rate:-1
  sleep 4
end

chords_ring = (ring, chord(:Eb1, :m, invert: 2), chord(:B1, :M), chord(:Db1, :m), chord(:Db1, :m),
                     chord(:Eb1, :m, invert: 2), chord(:B1, :M), chord(:Db1, :m), chord(:Db1, :m),
                     chord(:Eb1, :m, invert: 2), chord(:Bb1, :m7), chord(:Ab1, :m), chord(:Ab1, :m),
                     chord(:Eb1, :m, invert: 2), chord(:B1, :M), chord(:Db1, :m), chord(:Db1, :m)

)

live_loop :bass do

    sync :snare
    use_synth :fm
    play chords_ring.tick.choose, amp:2
    sleep (ring 0.75, 0.75, 0.5).tick
end

live_loop :some_synthd do
  use_synth :blade
  sync :snare
  with_fx :wobble, phase: 0.75 do
    with_transpose 12*3 do
      play_pattern_timed chords_ring.tick.shuffle, 0.25, attack:1, sustain:0.5, release: 1, amp:0.3
    end
  end
end

live_loop :chordd do
  sync :snare
  use_synth :tb303
  with_transpose 12*2 do
    with_fx :echo do |r|
      if one_in(6)
        control r, phase: 0.25, decay: 8
      else
        control r, mix:0
      end
      with_fx :reverb do
        play_chord chords_ring.tick, release:0.75
      end
    end
  end
end
